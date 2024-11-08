/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class HtmlBuilder {

    private static final String HTML_OPEN = "<html>";
    private static final String HTML_CLOSE = "</html>";
    private static final String BODY_OPEN = "<body>";
    private static final String BODY_CLOSE = "</body>";

    private static final String LOGO = "https://i.ibb.co/7pmpQ0Q/Whats-App-Image-2024-10-17-at-15-14-26.jpg";
    private static final String QR_CODE = "https://i.ibb.co/9vXCSXb/qr.jpg";

    public static String generateTableHelp(String title, String explanation, String[] headers, List<String[]> data) {
        // Logo HTML
        String logoHtml = "<img src='" + LOGO + "' alt='Logo' style='max-width: 400px; max-height: 200px; margin-bottom: 10px;'>";

        // Table headers HTML
        StringBuilder tableHeadersHtml = new StringBuilder();
        for (String header : headers) {
            tableHeadersHtml.append("<th style=\"border: 1px solid #ddd; padding: 12px; background-color: #00BFFF; color: #fff;\">").append(header).append("</th>");
        }

        // Table body HTML
        String tableBodyHtml = "";
        int rowNum = 0;
        for (String[] element : data) {
            String rowColor = (rowNum % 2 == 0) ? "#e6f7ff" : "#ffffff";  // Alternar celeste claro y blanco
            tableBodyHtml += "<tr style=\"border: 1px solid #ddd; background-color: " + rowColor + ";\">";
            for (String value : element) {
                tableBodyHtml += "<td style=\"border: 1px solid #ddd; padding: 12px;\">" + value + "</td>";
            }
            tableBodyHtml += "</tr>";
            rowNum++;
        }

        // HTML completo
        String html =
                "<div style=\"max-width: 800px; margin: 0 auto; padding: 20px; background-color: #e6f7ff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); text-align: center;\">" +
                        logoHtml +
                        "<h2 style=\"color: #00BFFF; margin-bottom: 10px;\">" + title + "</h2>" +
                        "<p style=\"font-size: 16px; color: #555; margin-bottom: 20px;\">" + explanation + "</p>" +
                        "<table style=\"width: 100%; border-collapse: collapse;\">" +
                        "<thead style=\"background-color: #00BFFF; color: #fff;\">" + tableHeadersHtml + "</thead>" +
                        "<tbody>" + tableBodyHtml + "</tbody>" +
                        "</table>" +
                        "</div>";

        return insertInHtml(html);
    }

    public static String generateTable(String title, String[] headers, List<String[]> data) {
        String tableHeadersHtml = "";
        for (String header : headers) {
            tableHeadersHtml += "<th style=\"border: 1px solid #ddd; padding: 12px; background-color: #00BFFF; color: #fff;\">" + header + "</th>";
        }

        String tableBodyHtml = "";
        int rowNum = 0;
        for (String[] element : data) {
            // Alternar colores de fondo para filas
            String rowColor = (rowNum % 2 == 0) ? "#e6f7ff" : "#ffffff";  // Celeste claro y blanco
            tableBodyHtml += "<tr style=\"border: 1px solid #ddd; background-color: " + rowColor + ";\">";
            for (String value : element) {
                tableBodyHtml += "<td style=\"border: 1px solid #ddd; padding: 12px; color: #000000;\">" + value + "</td>";
            }
            tableBodyHtml += "</tr>";
            rowNum++;
        }

        String logoHtml = "<img src=\"" + LOGO + "\" alt=\"Company Logo\" style=\"max-width: 400px; max-height: 200px;\">";

        String html =
                "<div style=\"max-width: 800px; margin: 0 auto; padding: 20px; background-color: #00BFFF; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                        "<center>" + logoHtml + "</center>" +
                        "<center><h2 style=\"color: #fff; margin-bottom: 10px;\">" + title + "</h2></center>" +
                        "<table style=\"width: 100%; border-collapse: collapse;\">" +
                        "<thead style=\"background-color: #00BFFF; color: #ffffff;\">" + tableHeadersHtml + "</thead>" +
                        "<tbody>" + tableBodyHtml + "</tbody>" +
                        "</table>" +
                        "</div>";

        return insertInHtml(html);
    }

    public static String generateText(String[] args) {
        StringBuilder accumulatedHtml = new StringBuilder(
                "<div style=\"border: 2px solid #00BFFF; padding: 20px; background-color: #e6f7ff; color: #00BFFF; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                        "<center><img src=\"" + LOGO + "\" alt=\"Logo\" style=\"width: 225px; height: 225px;\">" +
                        "<h2 style=\"color: #00BFFF; font-size: 24px;\">Informe de petición</h2></center>"
        );

        for (String arg : args) {
            accumulatedHtml.append("<p style=\"color: #00BFFF; font-size: 18px\">").append(arg).append("</p>");
        }

        accumulatedHtml.append("</div>");
        return insertInHtml(accumulatedHtml.toString());
    }

    public static String generatePaymentReport(String ventaId, String fechaPago, String monto, String metodoPago) {
        String accumulatedHtml =
                "<div style=\"border: 2px solid #00BFFF; padding: 20px; background-color: #e6f7ff; color: #00BFFF; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">" +
                        "<center><img src=\"" + LOGO + "\" alt=\"Logo\" style=\"width: 225px; height: 225px;\">" +
                        "<h2 style=\"color: #00BFFF; font-size: 24px;\">Informe de pago</h2></center>" +
                        "<p style=\"color: #00BFFF;\">ID de Venta: " + ventaId + "</p>" +
                        "<p style=\"color: #00BFFF;\">Monto: $" + monto + "</p>" +
                        "<p style=\"color: #00BFFF;\">Fecha de Pago: " + fechaPago + "</p>" +
                        "<p style=\"color: #00BFFF;\">Método de Pago: " + metodoPago + "</p>" +
                        "<center><img src=\"" + QR_CODE + "\" alt=\"QR Code\" style=\"width: 330px; height: 462px;\"></center>" +
                        "</div>";

        return insertInHtml(accumulatedHtml);
    }

    public static String generateGrafica(String title, List<String[]> data) {
        StringBuilder labels = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (String[] element : data) {
            labels.append("'").append(element[0]).append("',");
            values.append(element[1]).append(",");
        }

        // Eliminar la última coma
        String labelsStr = labels.substring(0, labels.length() - 1);
        String valuesStr = values.substring(0, values.length() - 1);

        // Crear la configuración del gráfico
        String chartConfig = "{type:'pie',data:{labels:[" + labelsStr + "],datasets:[{data:[" + valuesStr + "]}]}}";

        // Codificar la configuración
        String encodedConfig = null;
        try {
            encodedConfig = URLEncoder.encode(chartConfig, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }

        // Generar el HTML para el gráfico con estilo
        String graficas = "<div style=\"border: 2px solid #00BFFF; padding: 20px; background-color: #e6f7ff; color: #00BFFF; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width: 800px; margin: 0 auto; text-align: center;\">" +
                "<center><img src=\"" + LOGO + "\" alt=\"Logo\" style=\"width: 225px; height: 225px;\">" +
                "<h2 style=\"color: #00BFFF; font-size: 24px;\">" + title + "</h2></center>" +
                "<div>" +
                "<img src=\"https://quickchart.io/chart?c=" + encodedConfig + "\" style=\"max-width: 100%; height: auto;\">" +
                "</div>\n" +
                "</div>";

        return insertInHtml(graficas);
    }

    private static String insertInHtml(String data) {
        return HTML_OPEN + BODY_OPEN + data + BODY_CLOSE + HTML_CLOSE;
    }
}