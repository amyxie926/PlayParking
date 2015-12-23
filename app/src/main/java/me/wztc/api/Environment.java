package me.wztc.api;

/**
 * Configuration for environment.
 */
public class Environment {

    private static final int DEF_TIMEOUT = 15000;

    private final String serviceSchema;
    private final String serviceHost;
    private final int servicePort;
    private final String serviceBasePath;
    private final int timeout;

    private final String account;
    private final String password;

    public Environment(String serviceSchema, String serviceHost, int servicePort,
                       String serviceBasePath, int timeout, String account, String password) {
        this.serviceSchema = serviceSchema;
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.serviceBasePath = serviceBasePath;
        this.timeout = timeout;

        this.account = account;
        this.password = password;
    }

    public Environment(String serviceSchema, String serviceHost, int servicePort,
                       String serviceBasePath, String account, String password) {
        this(serviceSchema, serviceHost, servicePort, serviceBasePath, DEF_TIMEOUT, account,
                password);
    }

    public Environment(String serviceHost, int servicePort, String serviceBasePath, int timeout,
                       String account, String password) {
        this("http", serviceHost, servicePort, serviceBasePath, timeout, account, password);
    }

    public Environment(String serviceHost, int servicePort, String serviceBasePath, String account,
                       String password) {
        this("http", serviceHost, servicePort, serviceBasePath, DEF_TIMEOUT, account, password);
    }

    public String getServiceSchema() {
        return serviceSchema;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public int getServicePort() {
        return servicePort;
    }

    public String getServiceBasePath() {
        return serviceBasePath;
    }

    public int getServiceTimeout() {
        return timeout;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
