package com.cog.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cog.api.model.Media;
import com.cog.api.model.Series;

@RestController
@RequestMapping("/api/1.0//media")
public class MediaController extends BaseController<Media> {
	
	public static final String UPLOAD_DIR_NAME = "upload";
	private Path rootLocation;

	@PostConstruct
	public void afterPropertiesSet() {
		this.rootLocation = Paths.get(this.cogApiProperties.getMediaRoot(), UPLOAD_DIR_NAME);
		try {
			if(Files.notExists(this.rootLocation)) {
				Files.createDirectories(this.rootLocation);
			}		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	public Media create(@Valid @RequestBody Media s, BindingResult result) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Series update(@PathVariable String id, @RequestBody Map<String, Object> updatedFields) {
		throw new UnsupportedOperationException();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/upload")
	public Media upload(@RequestParam("file") MultipartFile file) throws IOException {
		Path dest = this.store(file);
		String webPath = this.local2WebPath(dest);
		Media m = new Media();
		m.setPath(webPath);
		Date now = new Date();
		m.setCreated(now);
		m.setUpdated(now);
		this.mongoTemplate.insert(m);
		return m;
	}
	
	@Override
	public void delete(@PathVariable String id) {
		Media m = this.mongoTemplate.findAndRemove(this.queryOfById(id), Media.class);
		Path localPath = this.web2LocalPath(m.getPath());
		Path trashDir = this.rootLocation.resolve("trash");
		try {
			if (Files.notExists(trashDir)) {
				Files.createDirectory(trashDir);
			}
			if(Files.exists(localPath)) {
				Files.move(localPath, trashDir.resolve(localPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
			}			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Path store(MultipartFile file) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateStr = LocalDate.now().format(formatter);		
		Path dir = this.rootLocation.resolve(dateStr);
		if(Files.notExists(dir)) {
			Files.createDirectory(dir);
		}
		Path dest = dir.resolve(file.getOriginalFilename());
		if (Files.exists(dest)) {
			throw new IOException(
					String.format("File '%s' existed. Please change another file or use another filename.",
							file.getOriginalFilename()));
		}
		try {
			if (file.isEmpty()) {
				throw new IOException("Failed to store empty file " + file.getOriginalFilename());
			}
			Files.copy(file.getInputStream(), dest);
			return dest;
		} catch (IOException e) {
			throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}
	
	private String local2WebPath(Path localPath) {
		Path p = Paths.get(this.cogApiProperties.getMediaRoot());
		Path rlocal = p.relativize(localPath);
		Path webPath = Paths.get(this.cogApiProperties.getMediaWebRoot());
		webPath = webPath.resolve(rlocal);
		return webPath.toString().replace('\\', '/');
	}
	
	private Path web2LocalPath(String webPath) {
		Path webRoot = Paths.get(this.cogApiProperties.getMediaWebRoot());
		Path rwebPath = webRoot.relativize(Paths.get(webPath));
		return this.rootLocation.resolve(rwebPath);
	}

}
