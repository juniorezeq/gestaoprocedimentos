package br.com.asq.modelo.negocio;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_procedimento")
public class Procedimento implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "num_procedimento", length = 20, nullable = false)
	private int num_procedimento;
	@Column(name = "idade", length = 3, nullable = false)
	private int idade;
	@Column(name = "sexo", length = 10, nullable = false)
	private String sexo;
	@Column(name = "Permitido")
	private boolean permitido;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public boolean isPermitido() {
		return permitido;
	}
	public void setPermitido(boolean permitido) {
		this.permitido = permitido;
	}
	
	public String str_Permitido() {
		String str_permitido ;
		if (this.permitido) {
			str_permitido = "Sim";
		}
		else {
			str_permitido = "Não";
		}
		return str_permitido;
	}
	
}
