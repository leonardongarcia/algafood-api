package com.garcia.algafoodapi.api.v1.assembler;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.controller.UsuarioController;
import com.garcia.algafoodapi.api.v1.model.UsuarioModel;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UsuarioModelAssembler
    extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {

  @Autowired private ModelMapper modelMapper;

  @Autowired private AlgaSecurity algaSecurity;

  @Autowired private AlgaLinks algaLinks;

  public UsuarioModelAssembler() {
    super(UsuarioController.class, UsuarioModel.class);
  }

  @Override
  public UsuarioModel toModel(Usuario usuario) {
    UsuarioModel usuarioModel = createModelWithId(usuario.getId(), usuario);
    modelMapper.map(usuario, usuarioModel);

    if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
      usuarioModel.add(algaLinks.linkToUsuarios("usuarios"));

      usuarioModel.add(algaLinks.linkToGruposUsuario(usuario.getId(), "grupos-usuario"));
    }

    return usuarioModel;
  }

  //    usuario
  //        .getGrupos()
  //        .add(
  //            linkTo(methodOn(UsuarioGrupoController.class).listar(usuario))
  //                .withSelfRel());

  //        usuarioModel.add(linkTo(methodOn(UsuarioGrupoController.class).listar(usuario.getId()))
  //                .withRel("grupos-usuario"));
  //
  //        return usuarioModel;
  //    }

  @Override
  public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
    return super.toCollectionModel(entities).add(algaLinks.linkToUsuarios());
  }
}
