package filter;

import java.io.IOException;

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
       
    
    public FilterAutenticacao() {
        
    }
	
	public void destroy() {//Encerra os processos quando o servidor é parado
						   //Mata os processos de conexão com banco
	}
	
	/*Intercepta as requisições e as respostas no sistema
	 * Tudo que fizer no sistema vai fazer pro aqui
	 * Validação de autenticação
	 * Dar commit e rolback de transações do banco
	 * Validar e fazer redirecionamento de páginas*/
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
	}

	//Inicia os processos ou recursos quando o servidor sobe o projeto
	//Inicia a conexão com o banco
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
}
