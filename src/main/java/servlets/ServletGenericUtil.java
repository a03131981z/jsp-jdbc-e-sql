package servlets;
import java.io.Serializable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;
import dao.DAOUsuarioRepository;

public class ServletGenericUtil extends HttpServlet implements Serializable{

	private static final long serialVersionUID = 1L;
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	
	public Long getUserLogado(HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		String usuarioLogado = (String) session.getAttribute("usuario");
		
		return daoUsuarioRepository.consultaUsuarioLogado(usuarioLogado).getId();
	}
}
