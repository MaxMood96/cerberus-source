/**
 * Cerberus Copyright (C) 2013 - 2025 cerberustesting
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cerberus.core.exception;

import org.cerberus.core.engine.entity.MessageGeneral;

/**
 * {Insert class description here}
 *
 * @author Tiago Bernardes
 * @version 1.0, 24/06/2013
 * @since 2.0.0
 */
public class CerberusException extends Exception {

    private MessageGeneral MessageError;

    public CerberusException(MessageGeneral message, Throwable throwble) {
        super(throwble);
        this.MessageError = message;
    }

    public CerberusException(MessageGeneral message) {
        this.MessageError = message;
    }

    public MessageGeneral getMessageError() {
        return MessageError;
    }

    public void setMessageError(MessageGeneral MessageError) {
        this.MessageError = MessageError;
    }

    @Override
    public String getMessage(){
        return this.MessageError.getDescription() + ". " + super.getMessage();
    }
}
