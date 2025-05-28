package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.CafeteriaNotFoundException;
import br.edu.ifpb.academico.Amantes_Cafezinho.errors.UserIsNotCafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class CafeteriaService {

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    public Cafeteria resgatarCafeteriaPorCNPJ(String cnpj) {
        return cafeteriaRepository.findByCNPJ(cnpj).orElseThrow(() -> new CafeteriaNotFoundException(cnpj));
    }

    public Cafeteria findCafeteriaByUser(User user) {
        return cafeteriaRepository.findByUser(user).orElseThrow(() -> new UserIsNotCafeteria(user));
    }

    public void save(Cafeteria cafeteria) {
        cafeteriaRepository.save(cafeteria);
    }

    public void savePhoto(Cafeteria cafeteria, MultipartFile file) throws IOException {
        List<String> allowedFileTypes = List.of("image/jpeg", "image/png", "image/webp");

        if(file.isEmpty()) {
            throw new IOException("File is empty");
        }
        if(file.getOriginalFilename() == null) {
            throw new IOException("Error obtaining the name of the file");
        }
        if(!allowedFileTypes.contains(file.getContentType())) {
            throw new IOException("type of the file is not allowed");
        }

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        Path dir = Paths.get("uploads");
        Path destination = dir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        cafeteria.setPhotoPath("/uploads/" + uniqueFileName);
        this.save(cafeteria);
    }
}
