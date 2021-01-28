package br.com.asq.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.asq.modelo.dao.AutorizacaoDao;
import br.com.asq.modelo.dao.ProcedimentoDao;
import br.com.asq.modelo.negocio.Autorizacao;
import br.com.asq.modelo.negocio.Procedimento;
import br.com.asq.modelo.negocio.Usuario;
import br.com.asq.tx.Transacional;

@Named
@ViewScoped
public class AutorizacaoBean  implements Serializable{
	 /*Ezequiel - Classe Bean Aprovacao */
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AutorizacaoDao autorizacaoDao;
	@Inject
	private HttpSession session;
	@Inject
	private ProcedimentoDao procedimentoDao;
	private Autorizacao autorizacao = new Autorizacao();
	private List<Autorizacao> autorizacoes;
	private Usuario usuario;
	private static final String USUARIO_LOGADO = "usuarioLogado";
	private int num_procedimento;
	private String id;
	private int idade;
	private String sexo;
	private boolean atualizacao;
	
	public boolean isAtualizacao() {
		return atualizacao;
	}

	public void setAtualizacao(boolean atualizacao) {
		this.atualizacao = atualizacao;
	}

	@PostConstruct
	public void init() {
		autorizacoes = new ArrayList<Autorizacao>();
		atualizacao = false;
	}

	@Transacional
	public String iniciarCadastro() {
		return "/view/autorizacoes.xhtml?faces-redirect=true";
	}
	
	@Transacional
	public String consultarTodos() {
		return "/view/consultar.xhtml?faces-redirect=true";
	}
	
	@Transacional
	public List<Autorizacao> getAprovacoes() {
		List <Autorizacao> aprovacoes = new ArrayList<Autorizacao>();
		aprovacoes = autorizacaoDao.listarTodos();
		return aprovacoes;
	}
	
	@Transacional
	public void buscaAutorizacao() {
		    try {
		    	if(this.id !="") {
		    		Autorizacao recuperar = autorizacaoDao.buscaPorId(Long.parseLong(this.id));
					String mensagem;
					if (recuperar != null) {
						autorizacao = recuperar;
						num_procedimento = autorizacao.getProcedimento().getNum_procedimento();
						atualizacao = true;
					}else {
						autorizacao = new Autorizacao();
						atualizacao = false;
						mensagem = "Autorização ID: " + this.id + " Não foi Localizada" ;
						mensagemErro(mensagem);	
						this.id = null;
					}
		    	}else {
		    		num_procedimento = 0;
		    		autorizacao = new Autorizacao();
		    		atualizacao = false;
		    		this.id = null;
		    	}
		    	
				
			} catch (NumberFormatException e) {
				String mensagem;
				mensagem = "Ocorreu erro durante a excução do processo, Erro:"+ e.getMessage(); 
				mensagemErro(mensagem);	
				e.printStackTrace();
			}
	}
	
	
	@Transacional
	public String apagar() {
		if (atualizacao==true) {
			String mensagem;
			autorizacaoDao.remove(autorizacao);
			mensagem = "registro apagado!";
			autorizacao = new Autorizacao();
			mensagemSucesso(mensagem);
			num_procedimento = 0;	
			this.id = null;
		}
		return null;
	}
	
	
	@Transacional
	public String gravar() {		
		usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
		autorizacao.setUsuario(usuario);
		idade = autorizacao.getIdade();
		sexo = autorizacao.getSexo();
		String mensagem = "";
		String mensagemErro = "";
		String mensagemSucesso = "";
		boolean validaIdade = false;
		boolean validaSexo = false;
		boolean validaNumero = false;
		
		
		List<Procedimento> procedimentos = procedimentoDao.buscarNumero(this.num_procedimento);
		Procedimento procedimento = new Procedimento();
		boolean recuperou = false;
		if (!procedimentos.isEmpty()) {
			for (int i = 0; i< procedimentos.size(); i++) {
				Procedimento recuperado = procedimentos.get(i);
				if (recuperado.getIdade() == idade && recuperou == false && recuperado.getSexo().equals(sexo)) {
						procedimento = recuperado;
						recuperou = true;
						mensagemSucesso = "Procedimento Liberado!";
						validaIdade = true;
						validaSexo = true;
				}
				if (recuperado.getIdade() == idade && recuperou == false && !recuperado.getSexo().equals(sexo)) {
					validaIdade = true;
					validaSexo = false;
				}
				
				if (recuperado.getIdade() != idade && recuperou == false && recuperado.getSexo().equals(sexo)) {
					validaSexo = true;
					validaIdade = false;
				}
			}
		}else {
			validaNumero = true;
		}
		
		if (recuperou==true) {
			autorizacao.setProcedimento(procedimento);
			try {
				if (atualizacao==true) {
					autorizacaoDao.atualiza(autorizacao);
					mensagem = "Autorização Atualizada com sucesso!";
					autorizacao = new Autorizacao();
					mensagemSucesso(mensagem);
					num_procedimento = 0;	
					this.id = null;
				}else {
					autorizacaoDao.adiciona(autorizacao);
					mensagem = "Autorização Registrada com sucesso!";
					autorizacao = new Autorizacao();
					mensagemSucesso(mensagem);
					num_procedimento = 0;
					this.id = null;
				}
				
			} catch (Exception e) {
				mensagem = "Erro ao gravar o registro! " + e.getMessage();
				mensagemErro(mensagem);
				e.printStackTrace();
			}
		}else {
			if(validaNumero == true) {
				mensagemErro = " Procedimento código " + this.num_procedimento +" não foi localizado! ";
			}else {
				if (validaIdade==false && validaSexo == false) {
					mensagemErro = "Este procedimento não é permitido para a idade e nem sexo informado.";
				}
				if (validaIdade==false && validaSexo == true ) {
					mensagemErro = "Este procedimento não é permitido para a idade informada.";
				}
				if (validaSexo==false && validaIdade == true) {
					mensagemErro = "Este procedimento não é permitido para sexo informado.";
				}
			}
			mensagemErro(mensagemErro );
		}
		return null;
	}

	public Autorizacao getAutorizacao() {
		return autorizacao;
	}

	public void setAutorizacao(Autorizacao autorizacao) {
		this.autorizacao = autorizacao;
	}


	public int getNum_procedimento() {
		return num_procedimento;
	}

	public void setNum_procedimento(int num_procedimento) {
		this.num_procedimento = num_procedimento;
	}

	private void mensagemSucesso(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!!", mensagem));
	}

	private void mensagemErro(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
