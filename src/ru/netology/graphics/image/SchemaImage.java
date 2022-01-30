package ru.netology.graphics.image;

public class SchemaImage implements TextColorSchema{
    String schema = "$ @ M X 8 3 2 0 & # * + \" - , ."; //"$ @ M X 8 3 2 0 & # * + \" - , ."; "\u2587 \u25CF \u25C9 \u25CD \u25CE \u25CB \u2609 \u25CC - ";

    public SchemaImage(String schema) {
        this.schema = schema;
    }

    public SchemaImage() {
    }

    @Override
    public char convert(int color) {
        final String[] s = schema.split(" "); //"▇●◉◍◎○☉◌-";

        return s[(s.length - Math.abs(color * s.length / 256)) - 1].charAt(0);
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
