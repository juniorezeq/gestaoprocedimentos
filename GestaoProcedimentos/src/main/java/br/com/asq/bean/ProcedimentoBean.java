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

import br.com.asq.modelo.dao.ProcedimentoDao;
import br.com.asq.modelo.negocio.Procedimento;
import br.com.asq.tx.Transacional;

@Named
@ViewScoped
public class ProcedimentoBean  implements Serializable{
	 /*Ezequiel - Classe Bean Procedimento */
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProcedimentoDao procedimentoDao;
	private Procedimento procedimento = new Procedimento();
	private List<Procedimento> procedimentos;
	private int num_procedimento;
	private int idade;
	private String sexo;
	boolean recuperou = false;
	
	@PostConstruct
	public void init() {
		procedimentos = new ArrayList<Procedimento>();
			
	}
	
	@Transacional
	public String buscarProcedimento(){
		boolean validaIdade = false;
		boolean validaSexo = false;
		boolean validaNumero = false;
		String mensagemErro= "";
		String mensagemSucesso = "";
		recuperou = false;
		List<Procedimento> procedimentos = procedimentoDao.buscarNumero(this.num_procedimento);
		Procedimento procedimento = new Procedimento();	
		System.out.println("foram recuperados " + procedimentos.size() + " procedimentos com o código" + this.num_procedimento  );
		if (!procedimentos.isEmpty()) {
			for (int i = 0; i< procedimentos.size(); i++) {
				Procedimento recuperado = procedimentos.get(i);
				if (recuperado.getIdade() == idade && recuperou == false && recuperado.getSexo().equals(sexo)) {
						procedimento = recuperado;
						recuperou = true;
						mensagemSucesso = "Este procedimento está liberado para as condições informadas!";
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
			mensagemSucesso(mensagemSucesso);
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
					mensagemErro = "Este procedimento não é permitido para Sexo informado.";
				}
			}
			
			mensagemErro(mensagemErro );
		}
			
		
		return null;
	}
	
	@Transacional
	public String iniciarCadastro() {
		return "/view/procedimentos.xhtml?faces-redirect=true";
	}

	@Transacional
	public List<Procedimento> getProcedimentos() {
		List <Procedimento> procedimentos = new ArrayList<Procedimento>();
		procedimentos = procedimentoDao.listarTodos();
		return procedimentos;
	}
	
	@Transacional
	public List<Procedimento> procedimentosCadastrados() {
		List <Procedimento> procedimentos = new ArrayList<Procedimento>();
		procedimentos = procedimentoDao.listarTodos();
		return procedimentos;
	}



	public int getNum_procedimento() {
		return num_procedimento;
	}

	public void setNum_procedimento(int num_procedimento) {
		this.num_procedimento = num_procedimento;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Procedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(Procedimento procedimento) {
		this.procedimento = procedimento;
	}
	
	
	private void mensagemSucesso(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!!", mensagem));
	}

	private void mensagemErro(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));

	}
	
	
}
