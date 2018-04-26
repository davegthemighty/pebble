package com.mitchellbosecke.pebble.attributes;

import com.mitchellbosecke.pebble.error.AttributeNotFoundException;

import java.util.List;

public class ListResolver implements AttributeResolver {

  @Override
  public ResolvedAttribute resolve(Object instance,
                                   Object attributeNameValue,
                                   Object[] argumentValues,
                                   boolean isStrictVariables,
                                   String filename,
                                   int lineNumber) {
    if (argumentValues == null && instance instanceof List) {
      String attributeName = String.valueOf(attributeNameValue);

      @SuppressWarnings("unchecked") List<Object> list = (List<Object>) instance;

      int index = this.getIndex(attributeName);
      int length = list.size();

      if (index < 0 || index >= length) {
        if (isStrictVariables) {
          throw new AttributeNotFoundException(null,
                  "Index out of bounds while accessing array with strict variables on.",
                  attributeName, lineNumber, filename);
        } else {
          return () -> null;
        }
      }

      return () -> list.get(index);
    }
    return null;
  }

  private int getIndex(String attributeName) {
    try {
      return Integer.parseInt(attributeName);
    }
    catch (NumberFormatException e) {
      return -1;
    }
  }
}
