package com.russozaripov.apigateway.mapper;//package com.russozaripov.springwebfluxsecurityesuleimanov.mapper;
//
//import com.russozaripov.springwebfluxsecurityesuleimanov.dto.UserDTO;
//import com.russozaripov.springwebfluxsecurityesuleimanov.entity.UserEntity;
//import org.mapstruct.InheritInverseConfiguration;
//import org.mapstruct.Mapper;
//
//@Mapper(componentModel = "spring")// указываем что нужно автоматически создать spring bean и в дальнейшем мы сможем его везде внедрять
//public interface UserMapper {
//
//    UserDTO map(UserEntity userEntity);
//    @InheritInverseConfiguration
//    UserEntity map(UserDTO userDTO);
//    // с помощью mapStruct мы сможем избежать много рутинного кода и автоматически конвертировать сущность в dto и обратно
//}
