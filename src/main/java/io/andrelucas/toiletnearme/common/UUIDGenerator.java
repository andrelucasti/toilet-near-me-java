package io.andrelucas.toiletnearme.common;

public interface UUIDGenerator {

   static String generateUUID() {
       return java.util.UUID.randomUUID().toString();
   }
}
