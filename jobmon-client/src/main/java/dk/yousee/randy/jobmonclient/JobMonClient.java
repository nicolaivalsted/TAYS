package dk.yousee.randy.jobmonclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import dk.yousee.randy.base.HttpPool;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * <bean id="jobMonClient" class="dk.yousee.randy.jobmonclient.JobMonClient">
 * <property name="jobMonHost"> <bean
 * class="org.springframework.jndi.JndiObjectFactoryBean"> <property
 * name="jndiName" value="jobMonHost"/> <property name="defaultObject"
 * value="194.239.10.173/jobmon-rest"/> </bean> </property> <property
 * name="jobMonPort"> <bean
 * class="org.springframework.jndi.JndiObjectFactoryBean"> <property
 * name="jndiName" value="jobMonPort"/> <property name="defaultObject"
 * value="8080"/> </bean> </property> </bean> @author jablo
 */
public class JobMonClient {
    private final static Logger log = Logger.getLogger(JobMonClient.class.getName());
    private final DateTimeFormatter dateparser = ISODateTimeFormat.dateTimeNoMillis();
    private final Gson gson;
    private String jobMonHost; //194.239.10.173:8080/jobmon-rest

    public void setJobMonHost(String jobMonHost) {
        this.jobMonHost = jobMonHost;
    }
    private int jobMonPort;

    public void setJobMonPort(int jobMonPort) {
        this.jobMonPort = jobMonPort;
    }
    
    private HttpPool httpPool;

    public void setHttpPool(HttpPool httpPool) {
        this.httpPool = httpPool;
    }
     
    public JobMonClient() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                final DateTimeFormatter dateparser = ISODateTimeFormat.dateTimeNoMillis();
                String date = json.getAsJsonPrimitive().getAsString();
                try {
                    return dateparser.parseDateTime(date).toDate();
                } catch (IllegalArgumentException e) {
                    throw new JsonParseException("Date " + date + " not parseable as ISO date formatted string", e);
                }
            }
        });
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                final DateTimeFormatter dateparser = ISODateTimeFormat.dateTimeNoMillis();
                StringBuffer sb = new StringBuffer();
                DateTime dt = new DateTime(src);
                dateparser.printTo(sb, dt);
                return new JsonPrimitive(sb.toString());
            }
        });
        gson = builder.create();
    }

    public RunningJobVo startRun(String jobName, Long estimatedRuntime) throws RestException {
        // post to job start and return running job meta data incl. url
        HttpEntity entity = null;
        try {
            HttpClient client = httpPool.getClient();
            
            URI uri = new URIBuilder("http://"+jobMonHost+":"+jobMonPort).setPath("/jobmon/run").addParameter("name", jobName).build();
            HttpPost post = new HttpPost(uri);
            post.setHeader("accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            StringEntity postDocument;
            if (estimatedRuntime != null) {
                RunningJobVo postEntity = new RunningJobVo();
                Date endTime = new Date(new Date().getTime() + estimatedRuntime);
                postEntity.setEstimateend(dateparser.print(new DateTime(endTime)));
                try {
                    String str = gson.toJson(postEntity);
                    log.log(Level.INFO, "Posting: {0}", str);
                    postDocument = new StringEntity(str);
                } catch (UnsupportedEncodingException ex) {
                    throw new RestException("Error building json request " + ex.getMessage(), ex);
                }
            } else {
                postDocument = new StringEntity("{}");
                log.log(Level.INFO, "Posting: empty");
            }

            post.setEntity(postDocument);
            //HttpHost target = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
            HttpResponse rsp = client.execute(post);
            entity = rsp.getEntity();
            if (rsp.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
                throw new RestException("HttpStatus jobmon: " + rsp.getStatusLine().getStatusCode());
            }
            String res = EntityUtils.toString(entity, "UTF-8");
            RunningJobVo fromJson = gson.fromJson(res, RunningJobVo.class);
            return fromJson;
        } catch (IOException ex) {
            throw new RestException(ex);
        } catch (JsonSyntaxException ex) {
            throw new RestException(ex);
        } catch (URISyntaxException ex) {
            throw new RestException(ex);
        } finally{
            if(entity!=null){
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    log.log(Level.WARNING, "Could not clean http");
                }
            }
        }
    }

    public RunningJobVo updateRun(RunningJobVo run) throws RestException {
        // post to job start and return running job meta data incl. url
        HttpEntity entity=null;
        try {
            CloseableHttpClient client = httpPool.getClient();
            URI uri = URIUtils.createURI("http", jobMonHost, jobMonPort, "/jobmon/run/" + run.getId(), null, null);

            HttpPut put = new HttpPut(uri);
            put.setHeader("accept", "application/json");
            put.setHeader("Content-Type", "application/json");
            StringEntity putDoc;
            try {
                String s = gson.toJson(run);
                log.log(Level.INFO, "Posting: {0}", s);
                putDoc = new StringEntity(s);
            } catch (UnsupportedEncodingException ex) {
                throw new RestException("Error building json request " + ex.getMessage());
            }
            put.setEntity(putDoc);
            HttpResponse rsp = client.execute(put);
            if (rsp.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
                throw new RestException(rsp.getStatusLine().getReasonPhrase());
            }
            entity = rsp.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            RunningJobVo fromJson = gson.fromJson(res, RunningJobVo.class);
            return fromJson;
        } catch (IOException ex) {
            throw new RestException(ex.getMessage());
        } catch (JsonSyntaxException ex) {
            throw new RestException(ex.getMessage());
        } catch (URISyntaxException ex) {
            throw new RestException(ex);
        } finally{
            if(entity!=null){
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    log.log(Level.WARNING, "Could not clean http");
                }
            }
        }
    }

	public List<RunningJobVo> findLatestJobs(String jobName) throws RestException {
        // post to job start and return running job meta data incl. url
        HttpEntity entity=null;
        try {
            CloseableHttpClient client = httpPool.getClient();
            URI uri = URIUtils.createURI("http", jobMonHost, jobMonPort, "/jobmon/run/byjobname/" + jobName, null, null);

            HttpGet put = new HttpGet(uri);
            put.setHeader("accept", "application/json");
            HttpResponse rsp = client.execute(put);
            if (rsp.getStatusLine().getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
                throw new RestException(rsp.getStatusLine().getReasonPhrase());
            }
            entity = rsp.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
           return gson.fromJson(res, new TypeToken<List<RunningJobVo>>() {}.getType());
        } catch (IOException ex) {
            throw new RestException(ex.getMessage());
        } catch (JsonSyntaxException ex) {
            throw new RestException(ex.getMessage());
        } catch (URISyntaxException ex) {
            throw new RestException(ex);
        } finally{
            if(entity!=null){
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    log.log(Level.WARNING, "Could not clean http");
                }
            }
        }
	}
}
