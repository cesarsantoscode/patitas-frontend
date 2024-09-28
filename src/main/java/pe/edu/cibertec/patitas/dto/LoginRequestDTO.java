package pe.edu.cibertec.patitas.dto;

public record LoginRequestDTO(String tipoDocumento, String numeroDocumento, String password) {
}
