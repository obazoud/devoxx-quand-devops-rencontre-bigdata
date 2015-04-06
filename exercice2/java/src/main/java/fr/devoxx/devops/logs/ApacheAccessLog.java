package fr.devoxx.devops.logs;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApacheAccessLog implements Serializable {
    private String host;
    private String client;
    private String user;
    private String dateTime;
    private String method;
    private String path;
    private String protocol;
    private Integer code;
    private Long size;
    private String referer;
    private String agent;
    private String agentFamily;

    public String getIpRange() {
        return host.substring(0, host.indexOf(".")) + ".x.x.x";
    }

    public ApacheAccessLog(String host, String client, String user, String dateTime, String method, String path, String protocol, Integer code, Long size, String referer, String agent, String agentFamily) {
        this.host = host;
        this.client = client;
        this.user = user;
        this.dateTime = dateTime;
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.code = code;
        this.size = size;
        this.referer = referer;
        this.agent = agent;
        this.agentFamily = agentFamily;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentFamily() {
        return agentFamily;
    }

    public void setAgentFamily(String agentFamily) {
        this.agentFamily = agentFamily;
    }

    // Example: 156.114.215.58 - - [19/Mar/2015:22:06:24 +0100] "GET /category/jewelry?from=0 HTTP/1.1" 200 63 "/item/finance/804" "Mozilla/5.0 (Windows NT 6.0; rv:10.0.1) Gecko/20100101 Firefox/10.0.1"
    private static final String ACCESS_LOG_PATTERN =
            "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+) (\\S+) (\\S+)\" (\\d{3}) (\\d+) \"(\\S+)\" \"(.+)\"";
    private static final Pattern PATTERN = Pattern.compile(ACCESS_LOG_PATTERN);

    private static final UserAgentStringParser parser = UADetectorServiceFactory
            .getCachingAndUpdatingParser();
    private static final Cache<String, ReadableUserAgent> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build();

    public static ApacheAccessLog parse(String line) {
        Matcher m = PATTERN.matcher(line);
        if (!m.find()) {
            throw new RuntimeException("Error parsing: " + line);
        }

        String agent = m.group(11);
        ReadableUserAgent userAgent = cache.getIfPresent(agent);
        if (userAgent == null) {
            userAgent = parser.parse(agent);
            cache.put(agent, userAgent);
        }

        return new ApacheAccessLog(
                m.group(1),m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), m.group(7),
                Integer.parseInt(m.group(8)), Long.parseLong(m.group(9)),
                m.group(10), m.group(11), userAgent.getFamily().getName());
    }
}
