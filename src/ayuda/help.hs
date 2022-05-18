<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN" "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">
 	<title>Ayuda Comicalia</title>
 	<maps>
 		<!-- Pagina por defecto al mostrar la ayuda -->
 		<homeID>menu</homeID>
 		<mapref location="mapa.jhm" />
 	</maps>
 	
 <!-- Vista de contenidos -->
 	<view>
 		<name>Tabla de contenidos</name>
 		<label>Tabla de contenidos</label>
 		<type>javax.help.TOCView</type>
 		<data>tablacontenidos.xml</data>
 	</view>
 
 <!-- Vista de indice  -->
 	<view>
 		<name>Índice</name>
 		<label>Índice</label>
 		<type>javax.help.IndexView</type>
 		<data>indice.xml</data>
 	</view>
 
 <!-- Vista de busqueda -->
 	<view>
 		<name>Buscar</name>
 		<label>Buscar</label>
		<type>javax.help.SearchView</type>
 		<data engine="com.sun.java.help.search.DefaultSearchEngine">
 			JavaHelpSearch
 		</data>
 	</view>
 
 <-- Vista de favoritos -->
	<view>
 		<name>Favoritos</name>
 		<label>Favoritos</label>
 		<type>javax.help.FavoritesView</type>
 	</view>
 
<-- Configuracion de la pantalla de ayuda -->
 	<presentation default="true" displayviews="false" displayviewimages="true">
 		<name>MainWindow</name>
 		<size width="900" height="600" /> 
 		<location x="300" y="150" />
 		<title></title>
 		<toolbar>
 			<helpaction image="BackwardIco">javax.help.BackAction</helpaction>
 			<helpaction image="Forward">javax.help.ForwardAction</helpaction>
 			<helpaction image="homeicon">javax.help.HomeAction</helpaction>
 			<helpaction image="imgFavoritos">javax.help.FavoritesAction</helpaction>
 		</toolbar>
		<image>logo</image>
 </presentation>
</helpset>