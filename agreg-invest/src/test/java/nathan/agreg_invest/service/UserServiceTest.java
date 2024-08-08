package nathan.agreg_invest.service;

import nathan.agreg_invest.dto.CreateUserDto;
import nathan.agreg_invest.dto.UpdateUserDto;
import nathan.agreg_invest.entity.User;
import nathan.agreg_invest.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock //criar mock para essa classe que dependemos (repository).
    private UserRepository userRepository;

    @InjectMocks //cria uma instancia da class Service injetando os Mocks ^^^^^^^.
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested //anotação para umas ''subclasse''
    class createUser {

        @Test
        @DisplayName("Should create a user correctly")
        void shouldCreateUserCorrectly(){
            //ARRANGE
            var user = new User(
                    UUID.randomUUID(),
                    "teste1",
                    "teste@teste.com",
                    "teste123",
                    Instant.now(),
                    null
            );
            //MOCK
            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            var input = new CreateUserDto(
                    "teste1",
                    "teste@teste.com",
                    "teste123");

            //ACT
            var output = userService.createUser(input);


            //ASSERT - Certificar que o "output" realmente veio e nao veio nulo, por exemplo
            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue(); //vai trazer o User que foi capturado acima (doReturn)

            assertEquals(input.username(), userCaptured.getUsername()); //primeiro parametro é o que eu espero que retorne e o segundo o que está retornando
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs(){
            //ARRANGE
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                    "teste1",
                    "teste@teste.com",
                    "teste123"
            );

            //ACT & ASSERT
            assertThrows(RuntimeException.class, () -> userService.createUser(input));

        }

    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get user by ID correctly when optional is present")
        void shouldGetUserByIdCorrectlyWhenOptionalIsPresent(){

            //ARRANGE
            var user = new User(
                    UUID.randomUUID(),
                    "teste1",
                    "teste@teste.com",
                    "teste123",
                    Instant.now(),
                    null
            );
            //MOCK
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            //ACT
            var output = userService.getUserById(user.getUserId().toString());

            //ASSERT - Desse modo garantimos que o User que foi passado nesse metodo acima, é o mesmo que foi passado no metodo "findById"
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by ID correctly when optional is empty")
        void shouldGetUserByIdCorrectlyWhenOptionalIsEmpty(){
            //ARRANGE
            var user = new User(
                    UUID.randomUUID(),
                    "teste1",
                    "teste@teste.com",
                    "teste123",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            //ACT
            var output = userService.getUserById(user.getUserId().toString());

            //ASSERT - Desse modo garantimos que o User que foi passado nesse metodo acima, é o mesmo que foi passado no metodo "findById"
            assertFalse(output.isEmpty());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
    }



    }

    @Nested
    class listUsers {
        @Test
        @DisplayName("Should return all users correctly")
        void shouldReturnAllUsersCorrectly() {
            var user = new User(
                    UUID.randomUUID(),
                    "teste1",
                    "teste@teste.com",
                    "teste123",
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            //MOCK
            doReturn(userList)
                    .when(userRepository)
                    .findAll();

            //ACT
            var output = userService.listUsers();

            //ASSERT
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("Should delete user correctly when it exists")
        void shouldDeleteUserCorrectlyWhenItExists(){
            //ARRANGE
            //MOCK
            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(userRepository)
                    .deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            //ACT
            userService.deleteById(userId.toString());

            //ASSERT
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));;

        }
        @Test
        @DisplayName("Should not delete user when it DON'T exists")
        void shouldNotDeleteUserWhenItDontExists(){
            //ARRANGE
            //MOCK
            doReturn(false)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());


            var userId = UUID.randomUUID();

            //ACT
            userService.deleteById(userId.toString());

            //ASSERT
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .existsById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(0)).deleteById(any()); //método para testar a cenário negativo, pode usar o any. No cenário possitivo, não é uma boa prática.

        }
    }

    @Nested
    class updateUserById {
        @Test
        @DisplayName("Should update user by ID when user exists, and, username and password are filled")
        void shouldUpdateUserByIdWhenUserExistsAndUsernameAndPasswordAreFilled() {

            //ARRANGE
            var updateUserDTO = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );
            var user = new User(
                    UUID.randomUUID(),
                    "teste1",
                    "teste@teste.com",
                    "teste123",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            //ACT
            userService.updateUserById(user.getUserId().toString(), updateUserDTO);

            //ASSERT
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();
            assertEquals(updateUserDTO.username(), userCaptured.getUsername());
            assertEquals(updateUserDTO.password(), userCaptured.getPassword());

            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                    .save(user);
        }
        @Test
        @DisplayName("Should not update user when user dont exists")
        void shouldNotUpdateUserWhenUserDontExists() {

            //ARRANGE
            var updateUserDTO = new UpdateUserDto(
                    "newUsername",
                    "newPassword"
            );
            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            //ACT
            userService.updateUserById(userId.toString(), updateUserDTO);

            //ASSERT
            assertEquals(userId, uuidArgumentCaptor.getValue());


            verify(userRepository, times(1))
                    .findById(uuidArgumentCaptor.getValue());

            verify(userRepository, times(0))
                    .save(any());
        }
    }
}