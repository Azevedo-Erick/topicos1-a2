package br.unitins.topicos.a2.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.unitins.topicos.a2.models.Cupom;
import br.unitins.topicos.a2.models.Empresa;
public class EmpresaDao implements Dao<Empresa>{

	@Override
	public boolean incluir(Empresa obj) {
		boolean result = false;
		String SQL = "insert into public.empresa( nome,sede_empresa,ceo,data_fundacao) values (?,?,?,?);";
		PreparedStatement stat = null;
		Connection conn=null;
		try {
			conn = Dao.getConnection();
			if(conn==null) {
				return result;
			}
			stat= conn.prepareStatement(SQL);
			stat.setString(1, obj.getNome());
			stat.setString(2, obj.getSedeEmpresa());
			stat.setString(3, obj.getCeo());
			stat.setDate(4, Date.valueOf(obj.getDataFundacao()));
			
			stat.execute();
			result = true;
		}catch(SQLException e) {
			System.out.println("Erro ao inserir empresa");
			e.printStackTrace();
			return result;
		}finally{
			try {
				conn.close();
			}catch(SQLException e){}
			try {
				stat.close();
			}catch(SQLException e){}
		}
		return result;
	}

	@Override
	public boolean alterar(Empresa obj) {
		Connection conn = Dao.getConnection();
		boolean result = false;
		if(conn==null) {
			return result;
		}
		String SQL = "UPDATE empresa SET nome=?,sede_empresa=?,ceo=?,data_fundacao=? WHERE id_empresa= ?;";
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(SQL);
			stat.setString(1, obj.getNome());
			stat.setString(2, obj.getSedeEmpresa());
			stat.setString(3, obj.getCeo());
			stat.setDate(4, Date.valueOf(obj.getDataFundacao()));
			stat.setInt(5, obj.getId());
			stat.execute();
			result = true;
		}catch(SQLException e){
			System.out.println("Erro ao atualizar os dados");
			e.printStackTrace();
			return result;
		}finally {
			try {
				conn.close();
			}catch (SQLException e) {
				
			}
			try {
				stat.close();
			}catch (SQLException e) {
				
			}
		}
		return result;
	}

	@Override
	public boolean excluir(Empresa obj) {
		Connection conn = Dao.getConnection();
		boolean resultado = false;
		if(conn==null) {
			return resultado;
		}
		PreparedStatement del = null;
		
		String SQL = "DELETE FROM empresa WHERE empresa.id_empresa = ?";
		try {
			del = conn.prepareStatement(SQL);
			del.setInt(1, obj.getId());
			del.execute();
			resultado = true;
		}catch(SQLException e) {
			e.printStackTrace();
			return resultado;
		}finally {
			try {
				conn.close();
			}catch(SQLException e){
				
			}
			try {
				del.close();
			}catch(SQLException e) {
				
			}
		}
		return resultado;
	}
	
	public Empresa verificarEmpresa(String nome) {
		Connection conn = Dao.getConnection();
		String sql = "SELECT id_empresa, nome, sede_empresa, ceo, data_fundacao FROM empresa WHERE nome = ?";
		if (conn == null)
			return null;

		PreparedStatement stat = null;
		ResultSet rs = null;
		Empresa empresa = null;

		try {
			stat = conn.prepareStatement(sql);
			stat.setString(1, nome);
			rs = stat.executeQuery();

			if (rs.next()) {
				empresa = new Empresa();
				empresa.setId(rs.getInt("id_empresa"));
				empresa.setNome((rs.getString("nome")));
				empresa.setSedeEmpresa(rs.getString("sede_empresa"));
				empresa.setCeo(rs.getString("ceo"));
				empresa.setDataFundacao(rs.getDate("data_fundacao").toLocalDate());

			}

		} catch (SQLException e) {
			empresa = null;
			e.printStackTrace();
		} finally {
			try {
				stat.close();
			} catch (SQLException e) {
			}
			try {
				rs.close();
			} catch (SQLException e) {
			}
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		return empresa;
	}
	
	public Empresa buscaPorId(int id) {
		Connection conn = Dao.getConnection();
		
		if(conn==null) {
			return null;
		}
		PreparedStatement stat = null;
		ResultSet rs = null;
		Empresa empresa=null;
		String SQL = "SELECT empresa.id_empresa, empresa.nome FROM empresa WHERE empresa.id_empresa=? ";
		try {
			stat = conn.prepareStatement(SQL);
			stat.setInt(1, id);
			
			rs = stat.executeQuery();
			
			if(rs.next()) {
				empresa = new Empresa();
				empresa.setId(rs.getInt(1));
				empresa.setNome(rs.getString(2));
			}
		}catch(SQLException e) {
			System.out.println("Erro ao buscar empresa");
			e.printStackTrace();
		}finally {
			try {
			rs.close();}catch(SQLException e) {}
			
			try {
				stat.close();}catch(SQLException e) {}
			
		}
		return empresa;
	}
	
	@Override
	public List<Empresa> obterTodos() {
		Connection conn = Dao.getConnection();
		if(conn==null) {
			return null;
		}
		List<Empresa> empresas=new ArrayList<Empresa>();
		String sql = "SELECT * FROM empresa ORDER BY id_empresa;";
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while(rs.next()) {
				empresas.add(new Empresa(
						rs.getInt(1), 
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getDate(5)!=null?rs.getDate(5).toLocalDate():null));
			}
			
		}catch(SQLException e) {
			System.out.println("Erro ao solicitar a lista de empresas");
			e.printStackTrace();
		}finally {
			try {
				conn.close();
				
			}catch(SQLException e) {}
			try {
				rs.close();
			}catch(SQLException e) {}
		}
		return empresas;
	}


}
