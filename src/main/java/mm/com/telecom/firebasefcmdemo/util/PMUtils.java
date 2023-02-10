package mm.com.telecom.firebasefcmdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
//import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Component
@Slf4j
public class PMUtils {

    public static RestTemplate getProxiedTemplate() {
        //MT mhr proxy ma ya loh
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.201.1.181", 3128));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }

//    public static <T> T toObject(String raw, Class T) {
//        if (StringUtils.isBlank(raw)) {
//            return null;
//        } else {
//            try {
//                return mapper.readValue(raw, T);
//            } catch (IOException var3) {
//                log.error("[{}] Can not parse, message: {}, rawStr: {}", new Object[]{var3.getClass().getSimpleName(), var3.getMessage(), raw});
//                return null;
//            }
//        }
//    }
}
