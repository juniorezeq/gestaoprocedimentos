package br.com.asq.modelo.negocio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_autorizacao")
public class Autorizacao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@ManyToOne
	private Usuario usuario;
	@Column(name = "num_auto", length = 10, nullable = false)
	private int num_auto;
	@Column(name = "nome", length = 50, nullable = false, unique = true)
	private String nome;
	@Column(name = "idade", length = 3, nullable = false)
	private int idade;
	@Column(name = "sexo", length = 10, nullable = false)
	private String sexo;
	@ManyToOne
	private Procedimento procedimento;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public int getNum_auto() {
		return num_auto;
	}
	public void setNum_auto(int num_auto) {
		this.num_auto = num_auto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	

}
