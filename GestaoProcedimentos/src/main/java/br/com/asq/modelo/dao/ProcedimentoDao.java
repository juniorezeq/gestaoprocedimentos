package br.com.asq.modelo.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import br.com.asq.modelo.negocio.Autorizacao;
import br.com.asq.modelo.negocio.Procedimento;

@Named
@RequestScoped
public class ProcedimentoDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private DAO<Procedimento> dao;
	
	@PostConstruct
	void init() {
		this.dao = new DAO<Procedimento>(this.em, Procedimento.class);
	}

	@Inject
	private EntityManager em;
	
	
	public List<Procedimento> buscaPorNumero(int numero) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select u from tb_procedimento u ");
		jpql.append(" where ");
		jpql.append("       u.num_procedimento = :pNumero ");
	
		TypedQuery<Procedimento> query = em.createQuery(jpql.toString() , Procedimento.class);
		
		query.setParameter("pNumero", numero);
		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	
	public List<Procedimento> buscarNumero(int numero){
		TypedQuery<Procedimento> query = em.createQuery(" select u from Procedimento u " + " where u.num_procedimento = :pNumero",
				Procedimento.class);

		query.setParameter("pNumero", numero);
		try {
			@SuppressWarnings("unused")
			List<Procedimento> resultado = query.getResultList();
			return resultado;
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	public List<Procedimento> listarTodos() {
		CriteriaQuery<Procedimento> query = em.getCriteriaBuilder().createQuery(Procedimento.class);
		query.select(query.from(Procedimento.class));
		List<Procedimento> lista = em.createQuery(query).getResultList();
		return lista;
	}
	
	
	public void adiciona(Procedimento procedimento) {
		dao.adiciona(procedimento);
	}

	public void atualiza(Procedimento procedimento){
		em.merge(procedimento);
	}

	public void remove(Procedimento procedimento) {
		dao.remove(procedimento);
	}

	public Procedimento buscaPorId(Long id) {
		return dao.buscaPorId(id);
	}

	public List<Procedimento> listaTodosPaginada(int firstResult, int maxResults) {
		return dao.listaTodosPaginada(firstResult, maxResults);
	}



}
