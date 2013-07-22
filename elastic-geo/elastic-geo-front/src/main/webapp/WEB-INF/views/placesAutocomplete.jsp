<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
		<link href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/smoothness/jquery-ui.css" rel="stylesheet">
		
		<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css" rel="stylesheet">
		<script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/js/bootstrap.min.js"></script>
		
		<%@ page isELIgnored="false" %>

		<style>
		.ui-autocomplete-loading {
			background: white url('http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/smoothness/images/ui-anim_basic_16x16.gif') right center no-repeat;
		}
		
		.hlt1 {
			font-weight: bold;
			text-decoration: underline;
		}
		</style>
		
		<script>
			$(function() {
				$["ui"]["autocomplete"].prototype["_renderItem"] = function(ul, item) {
					return $("<li></li>").data("item.autocomplete", item).append($("<a></a>").html(item.label)).appendTo(ul);
				};
					
				$("#place").autocomplete({
					source : function(request, response) {
						$.ajax({
							url : "http://localhost:9200/directory/place/_search",
							type : "POST",
							dataType : "json",
							data : JSON.stringify({
								query : {
									match : {
										name : request.term
									}
								},
								highlight : {
									tags_schema : "styled",
						        	fields : {
							            name : {
							            	number_of_fragments : 0
							            }
							        }
							    }
							}),
							processData: false,
							success : function(data) {
								response($.map(data.hits.hits, function(item) {
									return {
										label : item.highlight.name,
										value : item._source.name
									};
								}));
							}
						});
					},
					minLength : 3,
					open : function() {
						$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
					},
					close : function() {
						$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
					}
				});
			});
		</script>
	</head>
	<body>
		<div class="container-fluid">
			<h1>Elasticsearch avanzado: autocompletado</h1>
			
			<div class="row-fluid">
				<a class="btn" href="../elastic-geo-front">Inicio</a>
			</div>
			<div class="row-fluid">
				<input type="text" id="place" name="place" placeholder="Nombre del lugar" maxlength="255" class="span8" />
			</div>
		</div>
	</body>
</html>