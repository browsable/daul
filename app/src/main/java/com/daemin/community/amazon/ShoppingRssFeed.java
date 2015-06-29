package com.daemin.community.amazon;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by hernia on 2015-06-20.
 */
@Root(name = "rss")
public class ShoppingRssFeed {
    @Element(name = "channel")
    private ShoppingRssChannel channel;

    @Attribute
    private String version;

    public ShoppingRssChannel getChannel() {
        return channel;
    }
}