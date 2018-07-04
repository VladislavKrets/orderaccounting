package online.omnia.order;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 05.10.2017.
 */
public class LandingHandler {
    public Map<String, String> getLandingParameters(String url){

        System.out.println(url);
        Map<String, String> parametersMap = new HashMap<>();
        if (url == null || url.isEmpty()) return parametersMap;

        String[] urlParts = url.contains("?") ? url.split("\\?") : url.split("\\/");

        if (urlParts.length != 2){
            System.out.println("No /");
            System.out.println(Arrays.asList(urlParts));
            return parametersMap;
        }

        String parameters = urlParts[1];
        if (!parameters.contains("&")) {
            System.out.println("Not found &");
            String[] pair = parameters.split("=");
            if (pair.length == 0) return parametersMap;
            if (pair.length == 2) {
                parametersMap.put(pair[0], pair[1]);
            }
            else if (pair.length == 1) {
                parametersMap.put(pair[0], "");
            }
            return parametersMap;
        }
        String[] keyValuePairs = parameters.split("&");
        String[] pairs;

        for (String keyValuePair : keyValuePairs) {
            pairs = keyValuePair.split("=");
            if (pairs.length == 2) {
                parametersMap.put(pairs[0], pairs[1]);
            }
            else if (pairs.length == 1) {
                parametersMap.put(pairs[0], "");
            }
        }
        System.out.println("Parameters have been got");
        return parametersMap;
    }

    public OrderEntity orderCreator(Map<String, String> parameters) throws NoParametersException, UnsupportedEncodingException {

        if (parameters.isEmpty()) throw new NoParametersException();
        boolean isEmpty = false;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if ((entry.getValue() != null && !entry.getValue().isEmpty()) && !(entry.getKey().equals("order_id") && !entry.getValue().matches("\\d+"))) {
                isEmpty = true;
            }
        }
        if (!isEmpty) throw new NoParametersException();
        OrderEntity orderEntity = new OrderEntity();
        boolean hasParameters = false;
        if (parameters.containsKey("order_id")){
            String orderId = URLDecoder.decode(parameters.get("order_id"), "UTF-8");
            orderEntity.setOrderId(orderId.length() < 100 ? orderId : orderId.substring(0, 100));
            hasParameters = true;
        }
        if (parameters.containsKey("name")){
            String name = URLDecoder.decode(parameters.get("name"), "UTF-8");
            orderEntity.setName(name.length() < 200 ? name : name.substring(0, 200));
            hasParameters = true;
        }
        if (parameters.containsKey("adv")){
            AdvertsEntity advertsEntity = MySQLDaoImpl.getInstance().getAdvByName(URLDecoder.decode(parameters.get("adv"), "UTF-8"));
            orderEntity.setAdvId(advertsEntity != null ? advertsEntity.getId() : 0);
            hasParameters = true;
        }
        if (parameters.containsKey("address")){
            String address = URLDecoder.decode(parameters.get("address"), "UTF-8");
            orderEntity.setAddress(address.length() < 200 ? address : address.substring(0, 200));
            hasParameters = true;
        }
        if (parameters.containsKey("phone")){
            orderEntity.setPhone(parameters.get("phone").length() < 50 ? parameters.get("phone") : parameters.get("phone").substring(0, 50));
            hasParameters = true;
        }
        if (parameters.containsKey("country")){
            String country = URLDecoder.decode(parameters.get("country"), "UTF-8");
            orderEntity.setCountry(country.length() < 100 ? country : country.substring(0, 100));
            hasParameters = true;
        }
        if (parameters.containsKey("query")){
            String query = URLDecoder.decode(parameters.get("query"), "UTF-8");
            orderEntity.setQuery(query.length() < 500 ? query : query.substring(0, 500));
            hasParameters = true;
        }
        if (parameters.containsKey("clickid")){
            String clickid = URLDecoder.decode(parameters.get("clickid"), "UTF-8");
            orderEntity.setClickid(clickid.length() < 50 ? clickid : clickid.substring(0, 50));
            hasParameters = true;
        }
        if (!hasParameters) throw new NoParametersException();
        return orderEntity;
    }

}
