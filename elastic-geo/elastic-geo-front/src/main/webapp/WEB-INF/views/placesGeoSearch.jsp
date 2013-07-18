<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet">
		<script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
		<%@ page isELIgnored="false" %>
	</head>
	<body>
		<div class="container-fluid">
			<h1>Búsqueda Geolocalizada de Lugares</h1>
			
			<div class="span6">
				<form method="GET" action="places-geo-search/search" class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="type">Tipo de lugar</label>
						<div class="controls">
							<input type="text" id="type" name="type" placeholder="Restaurante, Monumento, Hospital" maxlength="255" class="span8" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="lat">Latitud</label>
						<div class="controls">
							<input type="text" id="lat" name="lat" placeholder="Latitud del lugar" class="span3" value="40.418761" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="lng">Longitud</label>
						<div class="controls">
							<input type="text" id="lng" name="lng" placeholder="Longitud del lugar" class="span3" value="-3.696792" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="radius">Radio de búsqueda</label>
						<div class="controls">
							<input type="text" id="radius" name="radius" placeholder="Radio de búsqueda en km" maxlength="3" value="2" class="span1" />
						</div>
					</div>
					
					<div class="control-group">
						<div class="controls">
							<button type="submit" class="btn">Buscar</button>
							<a class="btn" href="../elastic-geo-front">Inicio</a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</body>
</html>