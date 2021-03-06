package br.unitins.topicos.a2.controllers;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import br.unitins.topicos.a2.dao.UsuarioDao;
import br.unitins.topicos.a2.models.Usuario;
import br.unitins.topicos.a2.util.Session;
import br.unitins.topicos.a2.util.Utils;

@Named
@ViewScoped
public class LoginController implements Serializable{
	private static final long serialVersionUID = 1L;
	private Usuario usuario;
	
	public Usuario getUsuario() {
		if(usuario==null) {
			usuario = new Usuario();
		}
		return usuario;
	}
	
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	

	public void redirecionar(String pagina) {
		Utils.redirect(pagina);
	}
	
	public String login() {
		
		UsuarioDao daoCPF = new UsuarioDao();
		Usuario usuarioBanco = daoCPF.getUsuario(usuario.getEmail());
		if (usuarioBanco == null) {
			Utils.addErrorMessage("Usuario nao existe.");
			
		}
		usuario.setCpf(usuarioBanco.getCpf());

		UsuarioDao dao = new UsuarioDao();
		Usuario usu = dao.verificarUsuario(usuario.getEmail(), Utils.hash(usuario));
		
		if (usu != null) {
			// adicionando na sessao
			Session.getInstance().set("usuarioLogado", usu);
			Utils.redirect("/pages/index.xhtml");
		}
		Utils.addErrorMessage("Usuario, cpf ou senha inválido.");
		return null;
	}
}
