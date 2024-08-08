package nathan.agreg_invest.service;

import nathan.agreg_invest.dto.CreateAccountDto;

import nathan.agreg_invest.dto.CreateUserDto;

import nathan.agreg_invest.dto.UpdateUserDto;
import nathan.agreg_invest.entity.Account;
import nathan.agreg_invest.entity.BillingAddress;
import nathan.agreg_invest.entity.User;
import nathan.agreg_invest.repository.AccountRepository;
import nathan.agreg_invest.repository.BillingAddressRepository;

import nathan.agreg_invest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BillingAddressRepository billingAdressRepository;
    public UUID createUser (CreateUserDto createUserDTO) {
        //Necessário converter de createUserDTO para -> Entity (User) antes de salvar no banco.
        var entity = new User(UUID.randomUUID(),
                createUserDTO.username(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);

        //Dessa forma ele retorná apenas o Id.
        var userSaved = userRepository.save(entity);
        return userSaved.getUserId();

    }

    public Optional<User> getUserById (String userId){

        return userRepository.findById(UUID.fromString(userId)); //String é diferente de UUID. Entao precisa converter de STRING para UUID.

    }

    public List<User> listUsers (){
        return userRepository.findAll();
    }

    public void updateUserById(String userId,
                               UpdateUserDto updateUserDTO) {

        var id = UUID.fromString(userId);

        var userEntity = userRepository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateUserDTO.username() != null) {
                user.setUsername(updateUserDTO.username());
            }

            if (updateUserDTO.password() != null) {
                user.setPassword(updateUserDTO.password());
            }

            userRepository.save(user);
        }

    }

    public void deleteById(String userId) {

        var id = UUID.fromString(userId);

        var userExists = userRepository.existsById(id);

        if (userExists) {
            userRepository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDto createAccountDto) {

        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe"));

        if (isNull(user.getAccounts())){
            user.setAccounts(new ArrayList<>());
        }

        //Converter -- DTO -> ENTITY
        var account = new Account(
                UUID.randomUUID(),
                null,
                user,
                createAccountDto.description(),
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                accountCreated,
                createAccountDto.street(),
                createAccountDto.number()
        );

        billingAdressRepository.save(billingAddress);

    }

    public List<Account> findAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não existe"));

        return user.getAccounts();
    }
}
