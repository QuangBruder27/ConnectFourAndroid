package com.quangbruder.connectfourkbe.UserData;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

public class UserTest {

        UserImpl user;

        Context mockContext;
        SharedPreferences mockPrefs;
        SharedPreferences.Editor mockEditor;


        public void setUpForSetter() {
            user = new UserImpl();
            this.mockPrefs = new SPMockBuilder().createSharedPreferences();
            this.mockContext = Mockito.mock(Context.class);
            Mockito.when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        }

        public void setUpForGetter() {
            user = new UserImpl();
            mockPrefs = Mockito.mock(SharedPreferences.class);
            mockContext = Mockito.mock(Context.class);
            Mockito.when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        }

        @Test
        public void getNameTest() {
            setUpForGetter();
            System.out.println("Test getName in user");
            Mockito.when(mockPrefs.getString(anyString(), anyString())).thenReturn("Alice");
            user.preferencesName = mockPrefs;
            assert(user.getName(mockContext).equals("Alice"));
        }

        @Test
        public void setNameTest() {
            setUpForSetter();
            user.preferencesName = mockPrefs;
            //user.editorName = mockEditor;
            user.setName(mockContext, "Bob");
            assert(user.getName(mockContext).equals("Bob"));
        }

        @Test
        public void getWonTest() {
            setUpForGetter();
            Mockito.when(mockPrefs.getInt(anyString(), anyInt())).thenReturn(3);
            user.preferencesWon = mockPrefs;
            assert(user.getWon(mockContext) == 3);
        }

        @Test
        public void addWonTest() {
            setUpForSetter();
            user.preferencesWon = mockPrefs;
            user.addWon(mockContext);
            assert(user.getWon(mockContext) == 1);
        }

        @Test
        public void getLostTest() {
            setUpForGetter();
            Mockito.when(mockPrefs.getInt(anyString(), anyInt())).thenReturn(2);
            user.preferencesLost = mockPrefs;
            assert(user.getLost(mockContext) == 2);
        }

        @Test
        public void addLost() {
            setUpForSetter();
            user.preferencesLost = mockPrefs;
            user.addLost(mockContext);
            assert(user.getLost(mockContext) == 1);
        }
}