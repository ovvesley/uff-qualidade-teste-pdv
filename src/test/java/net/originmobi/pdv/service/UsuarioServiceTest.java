package net.originmobi.pdv.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import net.originmobi.pdv.model.GrupoUsuario;
import net.originmobi.pdv.model.Pessoa;
import net.originmobi.pdv.model.Usuario;
import net.originmobi.pdv.repository.UsuarioRepository;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GrupoUsuarioService grupoUsuarioService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private final LocalDate dataAtual = LocalDate.now();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setSenha("password");
        usuario.setData_cadastro(Date.valueOf(dataAtual));
    }

    @Test
    public void removeGrupo_RemoveGrupoExistente_Sucesso() {
        Usuario usuario = new Usuario();
        usuario.setCodigo(1L);
        usuario.setGrupoUsuario(new ArrayList<>());
        
        GrupoUsuario grupo = new GrupoUsuario();
        grupo.setCodigo(1L);
        usuario.getGrupoUsuario().add(grupo);

        when(usuarioRepository.findByCodigoIn(1L)).thenReturn(usuario);
        when(grupoUsuarioService.buscaGrupo(1L)).thenReturn(grupo);
        when(grupoUsuarioService.buscaGrupos(usuario)).thenReturn(usuario.getGrupoUsuario());

        String resultado = usuarioService.removeGrupo(1L, 1L);

        assertEquals("ok", resultado);
        assertFalse("Grupo não foi removido", usuario.getGrupoUsuario().contains(grupo));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void addGrupo_GrupoNaoExistente_AdicionaGrupo() {
        Usuario usuario = new Usuario();
        usuario.setCodigo(1L);
        usuario.setGrupoUsuario(new ArrayList<>());
        
        GrupoUsuario grupo = new GrupoUsuario();
        grupo.setCodigo(1L);

        when(usuarioRepository.findByCodigoIn(1L)).thenReturn(usuario);
        when(grupoUsuarioService.buscaGrupo(1L)).thenReturn(grupo);

        String resultado = usuarioService.addGrupo(1L, 1L);

        assertEquals("ok", resultado);
        assertTrue("Grupo não foi adicionado", usuario.getGrupoUsuario().contains(grupo));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void cadastrar_NovoUsuario_Sucesso() {
        Usuario usuario = new Usuario();
        usuario.setUser("testuser");
        usuario.setSenha("password");
        usuario.setGrupoUsuario(new ArrayList<>());

        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(1L);
        
        usuario.setPessoa(pessoa);
        
        when(usuarioRepository.findByUserEquals("testuser")).thenReturn(null);
        when(usuarioRepository.findByPessoaCodigoEquals(anyLong())).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String resultado = usuarioService.cadastrar(usuario);

        assertEquals("Usuário salvo com sucesso", resultado);
        assertNotNull("Senha não foi codificada", usuario.getSenha());
        assertTrue("Senha não está codificada em BCrypt", usuario.getSenha().startsWith("$2a$"));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void cadastrar_AtualizarUsuario_Sucesso() {
        usuario.setCodigo(1L);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String resultado = usuarioService.cadastrar(usuario);

        assertEquals("Usuário atualizado com sucesso", resultado);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void lista_RetornaListaUsuarios() {
        List<Usuario> usuarios = Collections.singletonList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.lista();

        assertEquals(1, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    public void buscaUsuario_UsuarioExistente_RetornaUsuario() {
        when(usuarioRepository.findByUserEquals("testuser")).thenReturn(usuario);

        Usuario resultado = usuarioService.buscaUsuario("testuser");

        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUser());
    }
}
