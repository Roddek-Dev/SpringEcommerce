package com.sena.ecommerce.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("/registro")
	public String createuser() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario, Model model) {
		LOGGER.info("Usuario a registrar: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		LOGGER.info("Accesos: {}", usuario);
		Optional<Usuario> userEmail = usuarioService.findByEmail(usuario.getEmail());
		LOGGER.info("usuario db obtenido {}", userEmail.get());
		if (userEmail.isPresent()) {
			session.setAttribute("idUsuario", userEmail.get().getId());
			if (userEmail.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
			}
		} else {
			LOGGER.info("Usuario no existe en la DB");
		}
		return "redirect:/";
	}

	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idUsuario");
		return "redirect:/";
	}
}
