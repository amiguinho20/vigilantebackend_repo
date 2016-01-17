package br.com.fences.vigilantebackend.rest;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.fences.fencesutils.conversor.InputStreamParaJson;
import br.com.fences.vigilantebackend.config.Log;
import br.com.fences.vigilantebackend.negocio.AlertaBO;
import br.com.fences.vigilantebackend.negocio.MapaBO;
import br.com.fences.vigilantebackend.negocio.UsuarioBO;
import br.com.fences.vigilanteentidade.negocio.Alerta;
import br.com.fences.vigilanteentidade.negocio.Endereco;
import br.com.fences.vigilanteentidade.negocio.Usuario;

@Log
@RequestScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VigilanteResource {
	
	@Inject
	private transient Logger logger;

	@Inject
	private UsuarioBO usuarioBO;
	
	@Inject
	private MapaBO mapaBO;
	
	@Inject
	private AlertaBO alertaBO;
	
	private Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter(Collection.class, new ColecaoJsonAdapter())
			.create();
    
    @PUT
    @Path("usuario/adicionar")
	public void usuarioAdicionar(InputStream ipFiltros)
	{
    	String json = InputStreamParaJson.converter(ipFiltros);
    	Usuario usuario = gson.fromJson(json, Usuario.class);
    	usuarioBO.adicionar(usuario);
	}
    
    @POST
    @Path("usuario/atualizar")
	public void usuarioAtualizar(InputStream ipFiltros)
	{
    	String json = InputStreamParaJson.converter(ipFiltros);
    	Usuario usuario = gson.fromJson(json, Usuario.class);
    	usuarioBO.atualizar(usuario);
	}
    
    
    @POST
    @Path("usuario/autenticar")
	public String usuarioAutenticar(InputStream ipFiltros)
	{
    	String json = InputStreamParaJson.converter(ipFiltros);
    	Usuario usuario = gson.fromJson(json, Usuario.class);
    	boolean autenticado = usuarioBO.autenticar(usuario);
    	return Boolean.toString(autenticado);
	}
    
    @GET
    @Path("usuario/consultar/{email}")
	public String usuarioConsultar(@PathParam("email") String email)
	{
    	Usuario usuario = usuarioBO.consultar(email);
    	String json = gson.toJson(usuario, Usuario.class);
    	return json;
	}
    
    @GET
    @Path("usuario/consultarSemImagem/{email}")
	public String usuarioConsultarSemImagem(@PathParam("email") String email)
	{
    	Usuario usuario = usuarioBO.consultarSemImagem(email);
    	String json = gson.toJson(usuario, Usuario.class);
    	return json;
	}
    
    @GET
    @Path("usuario/existeEmail/{email}")
	public String usuarioExisteEmail(@PathParam("email") String email)
	{
    	boolean existe = usuarioBO.existeEmail(email);
    	return Boolean.toString(existe);
	}
    
    @GET
    @Path("usuario/listarUsuarios")
	public String usuarioListarUsuarios()
	{
    	List<Usuario> usuarios = usuarioBO.listarUsuarios();
    	String json = gson.toJson(usuarios);
    	return json;
	}
    
    @GET
    @Path("mapa/consultarEndereco/{latitude}/{longitude}")
	public String mapaGeocodeReverse(@PathParam("latitude") Double latitude, @PathParam("longitude") Double longitude)
	{
    	Endereco endereco = mapaBO.consultarEndereco(latitude, longitude);
    	String json = gson.toJson(endereco);
    	return json;
	}
    
    @PUT
    @Path("alerta/emitir/{email}")
	public String alertaEmitir(@PathParam("email") String email, InputStream ipFiltros)
	{
    	String json = InputStreamParaJson.converter(ipFiltros);
    	Alerta alerta = gson.fromJson(json, Alerta.class);
    	
    	alertaBO.emitir(email, alerta);
    	
    	
    	return json;
	}
    
    @POST
    @Path("alerta/recepcionar/{idAlerta}/{email}")
	public void alertaRecepcionar(@PathParam("idAlerta") String idAlerta, @PathParam("email") String email)
	{
    	alertaBO.recepcionar(idAlerta, email);
 	}
    
    @GET
    @Path("alerta/listarAlertasPorEmailUsuario/{idUsuarioCriador}")
	public String alertaListarAlertasPorEmailUsuario(@PathParam("idUsuarioCriador") String idUsuarioCriador)
	{
    	List<Alerta> alertas = alertaBO.listarAlertasPorIdUsuario(idUsuarioCriador);
    	String json = gson.toJson(alertas);
    	return json;
	}
    
    @GET
    @Path("alerta/consultar/{id}")
	public String alertaConsultar(@PathParam("id") String id)
	{
    	Alerta alerta = alertaBO.consultar(id);
    	String json = gson.toJson(alerta);
    	return json;
	}
    
    @POST
    @Path("alerta/pesquisarLazy/{primeiroRegistro}/{registrosPorPagina}")
    public String alertaPesquisarLazy(
    		@PathParam("primeiroRegistro") int primeiroRegistro,
    		@PathParam("registrosPorPagina") int registrosPorPagina,
    		InputStream ipFiltros) 
    {
    	String json = InputStreamParaJson.converter(ipFiltros);
    	Map<String, String> mapFiltros = gson.fromJson(json, HashMap.class);
    	List<Alerta> alertas = alertaBO.pesquisarLazy(mapFiltros, primeiroRegistro, registrosPorPagina);
    	json = gson.toJson(alertas);
    	return json;
    }
 
    @POST
    @Path("alerta/pesquisarLazyContar") 
    public String alertaPesquisarLazyContar(InputStream ipFiltros) 
    {
    	String json = InputStreamParaJson.converter(ipFiltros);
		
    	Map<String, String> mapFiltros = gson.fromJson(json, HashMap.class);
    	int count = alertaBO.pesquisarLazyContar(mapFiltros);
    	
    	return Integer.toString(count);
    }    
    
    @POST
    @Path("alerta/contarNaoRecepcionado") 
    public String alertaContarNaoRecepcional(InputStream ipFiltros) 
    {
    	String json = InputStreamParaJson.converter(ipFiltros);
		
    	Map<String, String> mapFiltros = gson.fromJson(json, HashMap.class);
    	int count = alertaBO.contarNaoRecepcionado(mapFiltros);
    	
    	return Integer.toString(count);
    }    
    
}
