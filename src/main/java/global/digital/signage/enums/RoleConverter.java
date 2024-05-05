package global.digital.signage.enums;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role role) {
        if (role == null) {
            return null;
        }
        return String.format("%03d", Integer.parseInt(role.getCode()));
    }

    @Override
    public Role convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Role.fromCode(code);
    }
}
