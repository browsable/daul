package com.daemin.enumclass;

/**
 * Created by hernia on 2015-06-27.
 */
public enum SerialNumberGenerator {
        COUNT;
        public static int count = 0;

        public void initCount() {
            count = 0;
        }

        public int getCount() {
            return count;
        }

        public void next() {
            ++count;
        }
}
