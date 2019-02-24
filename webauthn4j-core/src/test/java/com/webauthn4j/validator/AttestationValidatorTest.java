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

package com.webauthn4j.validator;

import com.webauthn4j.response.attestation.AttestationObject;
import com.webauthn4j.response.attestation.authenticator.AAGUID;
import com.webauthn4j.response.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.response.attestation.statement.AttestationType;
import com.webauthn4j.response.attestation.statement.FIDOU2FAttestationStatement;
import com.webauthn4j.response.attestation.statement.PackedAttestationStatement;
import com.webauthn4j.test.TestUtil;
import com.webauthn4j.validator.attestation.statement.AttestationStatementValidator;
import com.webauthn4j.validator.attestation.statement.u2f.FIDOU2FAttestationStatementValidator;
import com.webauthn4j.validator.attestation.trustworthiness.certpath.NullCertPathTrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.ecdaa.NullECDAATrustworthinessValidator;
import com.webauthn4j.validator.attestation.trustworthiness.self.NullSelfAttestationTrustworthinessValidator;
import com.webauthn4j.validator.exception.BadAaguidException;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class AttestationValidatorTest {

    @Test
    public void validate_ecdaa(){
        AttestationStatementValidator attestationStatementValidatorMock = mock(AttestationStatementValidator.class);
        when(attestationStatementValidatorMock.supports(any())).thenReturn(true);
        when(attestationStatementValidatorMock.validate(any())).thenReturn(AttestationType.ECDAA);

        AttestationValidator attestationValidator = new AttestationValidator(
                Collections.singletonList(attestationStatementValidatorMock),
                new NullCertPathTrustworthinessValidator(),
                new NullECDAATrustworthinessValidator(),
                new NullSelfAttestationTrustworthinessValidator());

        RegistrationObject registrationObject = mock(RegistrationObject.class);
        AttestationObject attestationObject = mock(AttestationObject.class);
        when(attestationObject.getFormat()).thenReturn(PackedAttestationStatement.FORMAT);
        when(attestationObject.getAttestationStatement()).thenReturn(TestUtil.createFIDOU2FAttestationStatement());
        AuthenticatorData authenticatorData = mock(AuthenticatorData.class, RETURNS_DEEP_STUBS);
        when(attestationObject.getAuthenticatorData()).thenReturn(authenticatorData);
        when(registrationObject.getAttestationObject()).thenReturn(attestationObject);
        attestationValidator.validate(registrationObject);
    }

    @Test(expected = BadAaguidException.class)
    public void validateAAGUID(){
        AttestationValidator attestationValidator = new AttestationValidator(
                Collections.singletonList(new FIDOU2FAttestationStatementValidator()),
                new NullCertPathTrustworthinessValidator(),
                new NullECDAATrustworthinessValidator(),
                new NullSelfAttestationTrustworthinessValidator());

        AttestationObject attestationObject = mock(AttestationObject.class);
        when(attestationObject.getFormat()).thenReturn(FIDOU2FAttestationStatement.FORMAT);
        when(attestationObject.getAttestationStatement()).thenReturn(TestUtil.createFIDOU2FAttestationStatement());
        AuthenticatorData authenticatorData = mock(AuthenticatorData.class, RETURNS_DEEP_STUBS);
        when(authenticatorData.getAttestedCredentialData().getAaguid()).thenReturn(new AAGUID("fea37a71-08ce-479f-bf4b-472a93e2d17d"));
        when(attestationObject.getAuthenticatorData()).thenReturn(authenticatorData);
        attestationValidator.validateAAGUID(attestationObject);
    }

}