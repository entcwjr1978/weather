package com.lightcyclesoftware.weather.library.utils;

import android.content.Context;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import com.lightcyclesoftware.weather.library.R;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ewilliams on 11/7/15.
 */
public class Utils {

    public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
            return Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }

    public static String generateNonce() {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

    public static Map<String, String> buildQueryMap(URI uri) {
        UrlEncodedQueryString queryString = UrlEncodedQueryString.parse(uri);
        Map<String, String> result = new HashMap<String, String>();

        Iterator<String> itr = queryString.getNames();

        while (itr.hasNext()) {
            Object element = itr.next();
            result.put((String) element, queryString.getValues(((String) element)).get(0));
        }

        return result;
    }

    public static String buildTokenRequestSignature(Context context) throws UnsupportedEncodingException {
        TreeMap<String, String> map  = new TreeMap<String, String>();
        map.put("oauth_nonce", generateNonce());
        map.put("oauth_consumer_key", context.getString(R.string.oauth_consumer_key));
        map.put("oauth_signature_method", "HMAC-SHA1");
        map.put("oauth_version","1.0");
        map.put("oauth_timestamp", Long.toString(Calendar.getInstance().getTimeInMillis() / 1000));
        map.put("oauth_callback","oob");
        String signatureString = "GET&https%3A%2F%2Fwww.flickr.com%2Fservices%2Foauth%2Frequest_token&";
        String result = "https://www.flickr.com/services/oauth/request_token?";

        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            if (!result.equals("https://www.flickr.com/services/oauth/request_token?")) {
                result += "&";
                signatureString += URLEncoder.encode("&", "UTF-8");
            }
            Map.Entry me = (Map.Entry)iterator.next();
            signatureString += URLEncoder.encode((String)me.getKey(), "UTF-8") + URLEncoder.encode("=" + me.getValue(), "UTF-8");
            result += (String)me.getKey() + "=" + me.getValue();
        }

        System.out.println(signatureString);
        result += "&oauth_signature=" + URLEncoder.encode(hmacDigest(signatureString, context.getString(R.string.secret) + "&", Utils.HMAC_SHA1_ALGORITHM), "UTF-8");
        return result;
    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new SecureRandom();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
