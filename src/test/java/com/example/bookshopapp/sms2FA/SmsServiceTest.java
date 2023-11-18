package com.example.bookshopapp.sms2FA;

import com.example.bookshopapp.sms2FA.external.SMS_BY;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private SmsCodeRepository smsCodeRepository;
    @Mock
    private SMS_BY sms_by;

    @InjectMocks
    @Spy
    private SmsService smsService;

    @ParameterizedTest
    @CsvSource(value = {
            "+375(44) 756 563 5, 375447565635",
            "+375-44-756-56-35, 375447565635"
    })
    public void testFormatPhoneMethod(String phone, String expected) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(getFormatPhoneMethod().invoke(smsService, phone)).isEqualTo(expected);

    }

    private Method getFormatPhoneMethod() throws NoSuchMethodException {
        Method method = SmsService.class.getDeclaredMethod("formatPhone", String.class);
        method.setAccessible(true);
        return method;
    }
    @ParameterizedTest
    @CsvSource(value = {"123456, 123 456", "765567, 765 567"})
    public void testFormatCodeMethod(String code, String expected) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(smsService.formatCode(code)).isEqualTo(expected);

    }

    @Test
    public void testGenerateCodeMethod() {
        assertTrue(smsService.generateCode().matches("[0-9]{3}\\s[0-9]{3}"));
    }
}