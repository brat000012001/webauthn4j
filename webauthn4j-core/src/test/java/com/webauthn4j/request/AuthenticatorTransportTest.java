/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webauthn4j.request;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.webauthn4j.registry.Registry;
import org.assertj.core.api.Java6Assertions;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticatorTransportTest {

    Registry registry = new Registry();

    @Test
    public void create_test() {
        assertThat(AuthenticatorTransport.create(null)).isEqualTo(null);
        assertThat(AuthenticatorTransport.create("usb")).isEqualTo(AuthenticatorTransport.USB);
        assertThat(AuthenticatorTransport.create("nfc")).isEqualTo(AuthenticatorTransport.NFC);
        assertThat(AuthenticatorTransport.create("ble")).isEqualTo(AuthenticatorTransport.BLE);
    }

    @Test
    public void getValue_test() {
        assertThat(AuthenticatorTransport.USB.getValue()).isEqualTo("usb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_invalid_value_test() {
        AuthenticatorTransport.create("invalid");
    }

    @Test
    public void fromString_test() throws IOException {
        TestDTO dto = registry.getJsonMapper().readValue("{\"transport\":\"usb\"}", TestDTO.class);
        Java6Assertions.assertThat(dto.transport).isEqualTo(AuthenticatorTransport.USB);
    }

    @Test(expected = InvalidFormatException.class)
    public void fromString_test_with_invalid_value() throws IOException {
        registry.getJsonMapper().readValue("{\"transport\":\"invalid\"}", TestDTO.class);
    }


    public static class TestDTO{
        public AuthenticatorTransport transport;
    }

}