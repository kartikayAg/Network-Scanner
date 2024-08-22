import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;

public class NetworkDevices {
    public static void main(String[] args) {
        List<Device> devices = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isUp()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            Device device = new Device();
                            device.setIpAddress(inetAddress.getHostAddress());
                            device.setMacAddress(getMacAddress(networkInterface));
                            device.setOs(System.getProperty("os.name"));
                            devices.add(device);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        printDevices(devices);
    }

    private static String getMacAddress(NetworkInterface networkInterface) throws SocketException {
        byte[] mac = networkInterface.getHardwareAddress();
        if (mac == null) {
            return "No MAC address available";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        return sb.toString();
    }

    private static void printDevices(List<Device> devices) {
        for (Device device : devices) {
            System.out.println("IP Address: " + device.getIpAddress());
            System.out.println("MAC Address: " + device.getMacAddress());
            System.out.println("OS: " + device.getOs());
            System.out.println();
        }
    }
}

class Device {
    private String ipAddress;
    private String macAddress;
    private String os;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
