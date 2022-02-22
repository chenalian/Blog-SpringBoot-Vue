package site.alian.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.tomcat.util.codec.binary.Base64;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import site.alian.exception.AESException;
import site.alian.exception.TypeConverterException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class BlogUtil {

    // AES加密
    public static String AESEncrypt(String data) throws AESException {
        String target = null;
        try {
            Cipher cipher = Cipher.getInstance(Constant.CIPHER_ALGORITHM_CBC);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(Constant.AES_KEY.getBytes(), "AES");

            IvParameterSpec ivspec = new IvParameterSpec(Constant.AES_IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
//            target = new Base64().encodeToString(cipher.doFinal(plaintext));
            target =Base64.getEncoder().encodeToString(cipher.doFinal(plaintext));
        } catch (Exception e) {
            throw new AESException("AES加密失败！");
        }
        return target;
    }
    // AES解密
    public static String AESDecrypt(String data) throws AESException{
        String target = null;
        try {
            Cipher cipher = Cipher.getInstance(Constant.CIPHER_ALGORITHM_CBC);
            SecretKeySpec keyspec = new SecretKeySpec(Constant.AES_KEY.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(Constant.AES_IV.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(data));

            target = new String(original);
        } catch (Exception e) {
            throw new AESException("AES解密失败！");
        }
        return target;
    }

    // JSON字符串转Map
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> jsonToMap(String json) throws TypeConverterException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new TypeConverterException("JsonToMap转换异常！");
        }
    }

    // Map转JSON字符串
    public static String mapToJson(Map<String, Object> data) throws TypeConverterException{
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new TypeConverterException("MapToJson转换异常！");
        }
        return json;
    }

    // Object转Integer
    public static Integer objToInt(Object obj) {
        return Integer.parseInt(obj.toString());
    }

    // Markdown转HTML
    public static String mdToHtml(String md) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(md);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(context->new MarkdownUtil.CustomAttributeProvider())
                .build();
        return renderer.render(document);
    }
    // MD5加密
    public static String toMD5(String source) {
        // 空值校验
        if (source == null || source.length() < 1)
            return null;
        String target = null;
        try {
            // 通过MessageDigest获取MD5类型加密对象
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            // 获取字符串的字节数组使用MD5加密对象进行加密
            byte[] bytes = MD5.digest(source.getBytes());
            // 将加密的字节数组转换为16进制的字符串 (转换大写纯属为了有逼格~)
            return new BigInteger(1, bytes).toString(16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getToken(String key) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        long current = System.currentTimeMillis();
        long expires = Constant.TOKEN_EXPIRES_TIME * 1000 * 60;
        Date date = new Date(current + expires);
        HashMap<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return JWT.create()
                .withHeader(header)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    // 废弃 携带参数的Token
    //public static String getToken(String key, HashMap<String, String> param) {
    //    Algorithm algorithm = Algorithm.HMAC256(key);
    //    long current = System.currentTimeMillis();
    //    long expires = Constant.TOKEN_EXPIRES_TIME * 1000 * 60;
    //    Date date = new Date(current + expires);
    //    HashMap<String, Object> header = new HashMap<>();
    //    header.put("typ", "JWT");
    //    header.put("alg", "HS256");
    //    JWTCreator.Builder builder = JWT.create().withHeader(header);
    //    param.forEach(builder::withClaim);
    //    return builder
    //            .withExpiresAt(date)
    //            .sign(algorithm);
    //}

    public static void validateToken(String token, String key) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier require = JWT.require(algorithm).build();
        require.verify(token);
    }

    /*
    *
    * 根据不同的系统的不同存储不同的文件夹下面
    * 将图片存储在resource下面的static/static/img目录下面
    * 并返回图片的路径+名称
    *
    * */
    public static String saveImage(InputStream input,String type) {
        // 获取当前的时间戳并且拼接文件名
        String filename = String.valueOf(System.currentTimeMillis());
        filename = filename + "." + type;
        String osName = System.getProperty("os.name");
        File files = null;  //封面图片存放地址
        String path = "";  //获取项目相对路径
        if (osName.toLowerCase().startsWith("win")) {  //windows系统
            path = System.getProperty("user.dir") + "/src/main/resources/static/static/blogImage/";//获取项目相对路径
            files = new File(path + filename);
        } else {//linux系统
            //获取根目录
            //如果是在本地windows环境下，目录为项目的target\classes下
            //如果是linux环境下，目录为jar包同级目录
//            try {
//                path = ResourceUtils.getURL("classpath:").getPath();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            path="/usr/local/alian";
            files = new File(path + "/images/" + filename);
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int len = 0;
        byte buffer[] = new byte[4 * 1024];
        while (true) {
            try {
                if (!((len = input.read(buffer)) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.write(buffer, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (osName.toLowerCase().startsWith("win")) {
            return "static/blogImage/" + filename;
        } else {
            return  "images/" + filename;
        }
    }
}
