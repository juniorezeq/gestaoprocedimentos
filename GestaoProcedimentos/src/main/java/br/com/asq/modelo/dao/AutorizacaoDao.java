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

@Named
@RequestScoped
public class AutorizacaoDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private DAO<Autorizacao> dao;
	
	@PostConstruct
	void init() {
		this.dao = new DAO<Autorizacao>(this.em, Autorizacao.class);
	}

	@Inject
	private EntityManager em;
	
	
	public Autorizacao buscaPorNumero(int numero) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select u from tb_autorizacao u ");
		jpql.append(" where ");
		jpql.append("       u.num_auto = :pNumero ");
	
		TypedQuery<Autorizacao> query = em.createQuery(jpql.toString() , Autorizacao.class);
		
		query.setParameter("pNumero", numero);
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	
	public List<Autorizacao> listarTodos() {
		CriteriaQuery<Autorizacao> query = em.getCriteriaBuilder().createQuery(Autorizacao.class);
		query.select(query.from(Autorizacao.class));
		List<Autorizacao> lista = em.createQuery(query).getResultList();
		return lista;
	}
	
	
	public void adiciona(Autorizacao autorizacao) {
		dao.adiciona(autorizacao);
	}

	public void atualiza(Autorizacao autorizacao){
		em.merge(autorizacao);
	}

	public void remove(Autorizacao autorizacao) {
		dao.remove(autorizacao);
	}

	public Autorizacao buscaPorId(Long id) {
		return dao.buscaPorId(id);
	}

	public List<Autorizacao> listaTodosPaginada(int firstResult, int maxResults) {
		return dao.listaTodosPaginada(firstResult, maxResults);
	}



}