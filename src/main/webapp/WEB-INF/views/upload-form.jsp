<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.model.SimilarProduct" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0;
        }

        h1 {
            text-align: center;
            padding: 20px;
            background-color: #333;
            color: white;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            margin: 10px auto;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #333;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        img {
            max-width: 100px;
            max-height: 100px;
        }

        a {
            text-decoration: none;
            color: #007bff;
        }
    </style>
</head>
<body>
    <h1>Similar Products</h1>

    <%
        List<SimilarProduct> similarProducts = (List<SimilarProduct>) request.getAttribute("Similar_products");
        if (similarProducts != null && !similarProducts.isEmpty()) {
    %>
    <table>
        <tr>
            <th>Image</th>
            <th>Brand Name</th>
            <th>Name</th>
            <th>Price</th>
            <th>Reduced Price</th>
            <th>Category</th>
            <th>Sub Category</th>
            <th>Currency</th>
            <th>Gender</th>
            <th>URL</th>
            <th>Vendor</th>
        </tr>
        <%
            for (SimilarProduct product : similarProducts) {
        %>
        <tr>
            <td>
                <% if (product.getImages() != null && !product.getImages().isEmpty()) { %>
                    <!-- It's an image -->
                    <img src="<%= product.getImages().get(0) %>" alt="<%= product.getName() %>" />
                <% } %>
            </td>
            <td><%= product.brand_name %></td>
            <td><%= product.getName() %></td>
            <td><%= product.getPrice() %></td>
            <td><%= product.getReduced_price() %></td>
            <td><%= product.getCategory() %></td>
            <td><%= product.getSub_category() %></td>
            <td><%= product.getCurrency() %></td>
            <td><%= product.getGender()%></td>
            <td><a href="<%= product.getUrl() %>"><%= product.getUrl() %></a></td>
            <td><%= product.getVendor()%></td>
        </tr>
        <%
            }
        %>
    </table>
    <%
        }
    %>
</body>
</html>
