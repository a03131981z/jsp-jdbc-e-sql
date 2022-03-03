package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@WebFilter(urlPatterns = {"/principal/*"})//Intercepta todas as requisições que vierem do projeto ou mapeamento
public class FilterAutenticacao implements Filter {
       
	private static Connection connection;
    
    public FilterAutenticacao() {
        
    }
    					   //Mata os processos de conexão com banco
	public void destroy() {//Encerra os processos quando o servidor é parado
		try {
			connection.close();				   
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*Intercepta as requisições e as respostas no sistema
	 * Tudo que fizer no sistema vai fazer pro aqui
	 * Validação de autenticação
	 * Dar commit e rolback de transações do banco
	 * Validar e fazer redirecionamento de páginas*/
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			String usuarioLogado = (String) session.getAttribute("usuario");
			String urlParaAutenticar = req.getServletPath();//Url que está sendo acessada
			
			//Validar se está logado senão redireciona para a tela de login
			if(usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url="+urlParaAutenticar);
				request.setAttribute("msg", "Por favor realize o login!");
				redireciona.forward(request, response);
				return;//Para a execução e redireciona para o login
			
			}else {
				chain.doFilter(request, response);
			}
			
			connection.commit();//Deu tudo certo, então comita as alterações no banco de dados
			
		}catch(Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			
			}catch(SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	//Inicia os processos ou recursos quando o servidor sobe o projeto
	//Inicia a conexão com o banco
	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnectionBanco.getConnection();
	}
}
