package dk.yousee.tays.bbservice.restbase;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author m27236
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Device {
    private String deviceType;
    private String macAddress;
    private String ip;
    private String giAddress;
    private Boolean docsis3Capable;
    private Boolean wifiCapable;
    private String smpEquipmentType = "";
    //
    protected String classOfService;
    protected String manufacturer;
    protected String model;
    protected String firmwareVersion;
    protected String serialNo;
    //
    protected String ssid, downstream, maxCpe, psk, channel, upstream, vrfId, ownerid;
    //
    @XmlElement(name = "links")
    private List<Link> links = new ArrayList<Link>();

    public Device() {
    }

   /* public Device(RDUDevice rduDevice) {
        deviceType = rduDevice.getDeviceType();
        macAddress = rduDevice.getMacAddress().toString(MACaddress.fmtCOLON);
        if (rduDevice.getIp() != null)
            ip = rduDevice.getIp().getHostAddress();
        if (rduDevice.getGiAddress() != null)
            giAddress = rduDevice.getGiAddress().getHostAddress();
        if (rduDevice.isDocsis3Capable() != null)
            docsis3Capable = rduDevice.isDocsis3Capable();
        if (rduDevice.isWifiCapable() != null)
            wifiCapable = rduDevice.isWifiCapable();
        smpEquipmentType = rduDevice.getSmpEquipmentType();
        RDUDevice rdu = rduDevice;
        firmwareVersion = rdu.getFirmwareVersion();
        serialNo = rdu.getSerialNo();
        model = rdu.getModel();
        ssid = rdu.getSsid();
        psk = rdu.getPsk();
        channel = rdu.getChannel();
        classOfService = rdu.getClassOfService();
        downstream = rdu.getDownstream();
        upstream = rdu.getUpstream();
        manufacturer = rdu.getManufacturer();
        vrfId = rdu.getVrfId();
        ownerid = rdu.getOwnerid();
    }*/

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean isDocsis3Capable() {
        return docsis3Capable;
    }

    public String getGiAddress() {
        return giAddress;
    }

    public String getIp() {
        return ip;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getSmpEquipmentType() {
        return smpEquipmentType;
    }

    public Boolean isWifiCapable() {
        return wifiCapable;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClassOfService() {
        return classOfService;
    }

    public void setClassOfService(String classOfService) {
        this.classOfService = classOfService;
    }

    public String getDownstream() {
        return downstream;
    }

    public void setDownstream(String downstream) {
        this.downstream = downstream;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public void setMaxCpe(String maxCpe) {
        this.maxCpe = maxCpe;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPsk() {
        return psk;
    }

    public void setPsk(String psk) {
        this.psk = psk;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getUpstream() {
        return upstream;
    }

    public void setUpstream(String upstream) {
        this.upstream = upstream;
    }

    public void setDocsis3Capable(Boolean docsis3Capable) {
        this.docsis3Capable = docsis3Capable;
    }

    public void setGiAddress(String giAddress) {
        this.giAddress = giAddress;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setSmpEquipmentType(String smpEquipmentType) {
        this.smpEquipmentType = smpEquipmentType;
    }

    public void setWifiCapable(Boolean wifiCapable) {
        this.wifiCapable = wifiCapable;
    }

    public String getMaxCpe() {
        return maxCpe;
    }

    public String getVrfId() {
        return vrfId;
    }

    public void setVrfId(String vrfId) {
        this.vrfId = vrfId;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }
   
    public List<Link> getLinks() {
        return links;
    }

    public void addLink(Link link) {
        links.add(link);
    }
}
