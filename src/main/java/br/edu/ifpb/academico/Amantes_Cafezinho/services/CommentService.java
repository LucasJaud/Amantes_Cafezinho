package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Comment;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
