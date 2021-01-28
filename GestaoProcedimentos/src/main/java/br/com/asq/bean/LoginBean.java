package br.com.asq.bean;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.ApplicationScoped;
//import javax.faces.view.ViewScoped;
//import javax.enterprise.context.RequestScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.asq.modelo.dao.ProcedimentoDao;
import br.com.asq.modelo.dao.UsuarioDao;
import br.com.asq.modelo.negocio.Procedimento;
import br.com.asq.modelo.negocio.Usuario;
import br.com.asq.tx.Transacional;

@Named
@ViewScoped
public class LoginBean implements Serializable {
	
	private static final String USUARIO_LOGADO = "usuarioLogado";

	private static final long serialVersionUID = 1L;

	@Inject
	private HttpSession session;
	@Inject
	private UsuarioDao usuarioDao;
	private Usuario usuario;
	@Inject
	private ProcedimentoDao procedimentoDao;
	private Procedimento procedimento;

	public Usuario getUsuario() {
		return usuario;
	}

	@PostConstruct
	public void init() {
		System.out.println("LoginBean.init();");
		usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
		if (usuario == null) {
			usuario = new Usuario();
		}
	}
	
	@Transacional
	public String perfil(){
		return "/view/perfil/perfil.xhtml?faces-redirect=true";
	}
	

	@Transacional
	public String loga() {		
		Usuario usuarioAutenticado  = usuarioDao.buscaUsuarioPelaAutenticacao(this.usuario);
		if (usuarioAutenticado != null) {
			usuarioAutenticado.setDataDoUltimoAcesso(LocalDateTime.now());
			usuarioDao.atualiza(usuarioAutenticado);
			session.setAttribute(USUARIO_LOGADO, usuarioAutenticado);
			usuario = usuarioAutenticado;
			return "/view/logado.xhtml?faces-redirect=true";
		}
		List<Usuario> usuarios = usuarioDao.listarTodos();
		if (usuarios.isEmpty()) {
			Usuario acesso = this.usuario;
			acesso.setDataDoCadastro(LocalDateTime.now());
			if (acesso.getLogin().isEmpty() || acesso.getSenha().isEmpty()) {
				String mensagem = "Login e senha não podem ser vázios, favor preencher os campos";
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção!", mensagem));
			}else {
				acesso.setDataDoCadastro(LocalDateTime.now());
				usuarioDao.adiciona(acesso);
				String mensagem = "Cadastro realizado com sucesso!  login: " + acesso.getLogin() + " senha: " + acesso.getSenha();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção!", mensagem));	
				
				List<Procedimento> procedimentos = procedimentoDao.listarTodos();
				if (procedimentos.isEmpty()) {
					parametrizacao();
				}
			}	
		}else {
			String mensagem = "Usuário ou senha inválido!";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));
		}
		
		return null;
	}
	
	public String cadastrar() {
		Usuario acesso = new Usuario();
		acesso.setLogin(this.usuario.getLogin());
		acesso.setSenha(this.usuario.getSenha());
		return null;
	}
	
	public Date ultimoAcesso(){
		Date date = asDate(usuario.getDataDoUltimoAcesso());
		return date;
	}
	
	 public static Date asDate(LocalDateTime localDateTime) {
		    return (Date) Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		  }
	 
	@Transacional
	public String desloga() {
		session.removeAttribute(USUARIO_LOGADO);
		session.invalidate();
		usuario = new Usuario();
		return "/view/login/login.xhtml?faces-redirect=true";
	}
	
	
	@Transacional
	public List<Usuario> usuarios() {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios = usuarioDao.listarTodos();		
			return usuarios;
		}
	
	@Transacional
	public String novoUsuario() {
		return "/view/novousuario.xhtml?faces-redirect=true";
		}

	
	@Transacional
	public void atualizaUsuario() {
		try {
			usuarioDao.atualiza(usuario);
			String mensagem = "Alterado com sucesso";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!!", mensagem));

		} catch (Exception e) {
			String mensagem = "Erro não foi possivel atualizar";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));

		}
	}
	
	
	public void parametrizacao() {
		procedimento = new Procedimento();
		procedimento.setNum_procedimento(1234);
		procedimento.setIdade(10);
		procedimento.setSexo("M");
		procedimento.setPermitido(true);
		procedimentoDao.adiciona(procedimento);
		
		procedimento = new Procedimento();
		procedimento.setNum_procedimento(4567);
		procedimento.setIdade(20);
		procedimento.setSexo("M");
		procedimento.setPermitido(true);
		procedimentoDao.adiciona(procedimento);
		
		procedimento = new Procedimento();
		procedimento.setNum_procedimento(6789);
		procedimento.setIdade(10);
		procedimento.setSexo("F");
		procedimento.setPermitido(true);
		procedimentoDao.adiciona(procedimento);
		
		procedimento = new Procedimento();
		procedimento.setNum_procedimento(6789);
		procedimento.setIdade(10);
		procedimento.setSexo("M");
		procedimento.setPermitido(true);
		procedimentoDao.adiciona(procedimento);
		
		procedimento = new Procedimento();
		procedimento.setNum_procedimento(1234);
		procedimento.setIdade(20);
		procedimento.setSexo("M");
		procedimento.setPermitido(true);
		procedimentoDao.adiciona(procedimento);
		
		procedimento = new Procedimento();
		procedimento.setNum_procedimento(4567);
		procedimento.setIdade(30);
		procedimento.setSexo("F");
		procedimento.setPermitido(true);
		procedimentoDao.adiciona(procedimento);
	}
	
}
