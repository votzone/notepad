package wq.votzone.notebook.remote

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import android.util.StringBuilderPrinter
import wq.votzone.notebook.dbmodel.getAllAccount

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.net.UnknownHostException
import java.util.Enumeration
import java.util.logging.Level

import wq.votzone.notebook.util.DBUtil

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/8/6
 * Modification History:
 * Why & What modified:
 */

class SimpleServer @Throws(UnsupportedEncodingException::class)
constructor(private val content: ByteArray, encoding: String, MIMEType: String, port: Int) : Thread() {
    private val header: ByteArray
    private var port = 80

    internal var server: ServerSocket? = null

    @Throws(UnsupportedEncodingException::class)
    private constructor(data: String, encoding: String,
                        MIMEType: String, port: Int) : this(data.toByteArray(charset(encoding)), encoding, MIMEType, port) {
    }

    init {
        this.port = port
        val header = "HTTP/1.0 200 OK\r\n" +
                "Server: OneFile 1.0\r\n" +
                "Content-length: " + this.content.size + "\r\n" +
                "Content-type: " + MIMEType + "\r\n\r\n"
        this.header = header.toByteArray(charset("ASCII"))
    }

    fun stopServer() {
        if (server != null) {
            try {
                server!!.close()
            } catch (e: IOException) {

            }

        }
    }

    override fun run() = try {
        server = ServerSocket(this.port)


        while (true) {
            var connection: Socket? = null
            try {
                connection = server!!.accept()
                val out = BufferedOutputStream(connection!!.getOutputStream())
                val `in` = BufferedInputStream(connection.getInputStream())

                val request = StringBuffer()
                while (true) {
                    val c = `in`.read()
                    if (c == '\r'.toInt() || c .equals( '\n'.toInt()) || c.equals(-1)) {
                        break
                    }
                    request.append(c.toChar())

                }


                if (request.toString().indexOf("HTTP/") != -1) {
                    //HTTP/1.0及以后的协议，需要发送一个MIME首部
                    out.write(this.header)
                }

                out.write(this.content)
                out.flush()

            } catch (e: IOException) {
            } finally {
                if (connection != null) {
                    connection.close()
                }
            }
        }

    } catch (e: IOException) {

    }

    companion object {

        fun buildContent(): String {
            val sbuilder = StringBuilder()
            sbuilder.append("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "  \n" +
                    "  <head>\n" +
                    "    <title>我的密码</title>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\">\n" +
                    "    <meta content=\"telephone=no\" name=\"format-detection\" />\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div class=\"header\">\n" +
                    "      <h1>我的密码</h1>\n" +
                    "    </div>\n" +
                    "    <div>\n" +
                    "<table  border=\"1\" cellspacing=\"0\">\n" +
                    "  <tr>\n" +
                    "    <th width=\"100\">平台</th>\n" +
                    "    <th width=\"100\">账号</th>\n" +
                    "    <th width=\"100\">密码</th>\n" +
                    "    <th width=\"100\">等级</th>\n" +
                    "    <th width=\"100\">标签</th>\n" +
                    "    <th width=\"100\">其他</th>\n" +
                    "  </tr>")

            var list = getAllAccount()
            if(list.size <=0){
                sbuilder.append("没有数据")
                return """<!DOCTYPE html><html><head><title>我的密码</title><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"><meta content="telephone=no" name="format-detection" /></head><body><div class="header"><h1>没有密码</h1></div></body></html>"""
            }else{
                for (item in list){
                    sbuilder.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                            item.platform, item.username, item.password, item.level.des, item.tags, item.moreItems))
                }
            }


            sbuilder.append("</table>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>")
            return sbuilder.toString()
        }

        internal var instance: SimpleServer? = null
        fun simpleDemo(): String {
            val contentType = "text/html"
            val fileContent = buildContent()
            var data = ByteArray(0)
            try {
                data = fileContent.toByteArray(charset("utf-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            try {
                instance = SimpleServer(data, "", contentType, 8088)
                instance!!.start()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }


            return "8088"

        }

        fun stopSimpleServer() {
            if (instance != null) {
                instance!!.stopServer()
            }
        }

        /**
         * Helper

         * @return String
         */
        fun getLocalIpAddress(context: Context): String? {
            try {
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo

                var ipAddress = wifiInfo.ipAddress
                ipAddress = if (java.nio.ByteOrder.nativeOrder() == java.nio.ByteOrder.LITTLE_ENDIAN) Integer.reverseBytes(ipAddress) else ipAddress
                val inetAddress = InetAddress.getByAddress(BigInteger.valueOf(ipAddress.toLong()).toByteArray())
                return inetAddress.hostAddress.toString()

            } catch (e: Exception) {
                try {
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress) {
                                return inetAddress.hostAddress.toString()
                            }
                        }
                    }
                } catch (ex: SocketException) {
                    Log.e("", "Unable to obtain own IP address", e)
                }

            }

            return null
        }
    }
}
