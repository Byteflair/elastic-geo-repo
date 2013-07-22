<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
	<head>
		<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet">
		<script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
		<%@ page isELIgnored="false" %>
	</head>
	<body>
		<div class="container-fluid">
			<h1>Resultado de la búsqueda facetada de Lugares</h1>
			
			<div class="row-fluid">
				<a class="btn" href="../elastic-geo-front">Inicio</a>
			</div>
			
			<div class="row-fluid">
				<div class="span4">
					<ul>
					<c:forEach items="${list.facets}" var="facet">
						<li>${facet.field}</li>
						
						<ul>
							<c:forEach items="${facet.terms}" var="term">
								<li>${term.key} (${term.value})</li>
							</c:forEach>
						</ul>
					</c:forEach>
					</ul>
				</div>
				<div class="span8">
					<c:choose>
						<c:when test="${not empty list.list && list.list.size() > 0 }">
							<ul>
							<c:forEach items="${list.list}" var="place">
								<li>${place.name} - [${place.type}] - (${place.locality})</li>
							</c:forEach>
							</ul>
						</c:when>
						<c:otherwise>
							<span>No se han encontrado lugares</span>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</body>
</html>