package com.softserve.itacademy.service.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;

import java.io.IOException;

public class ToDoSerializer extends StdSerializer<ToDo> {

    public ToDoSerializer() {
        this(null);
    }

    public ToDoSerializer(Class<ToDo> t) {
        super(t);
    }


    @Override
    public void serialize(ToDo toDo, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("id", toDo.getId());
        jsonGenerator.writeStringField("title", toDo.getTitle());
        jsonGenerator.writeNumberField("owner_id", toDo.getOwner().getId());

        jsonGenerator.writeEndObject();
    }
}
