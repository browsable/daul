package com.daemin.community.amazon;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by hernia on 2015-06-20.
 */
@Root
public class ShoppingRssChannel {
    @Element
    private String title;
    @Element
    private String link;
    @Element(required = false)
    private String description;
    @Element(required = false)
    private String pubDate;
    @Element
    private String lastBuildDate;
    @Element(required = false)
    private String ttl;
    @Element(required = false)
    private String generator;
    @Element(required = false)
    private String language;
    @Element(required = false)
    private String copyright;
    @ElementList(inline=true)
    private List<ShoppingItem> items;
    public List<ShoppingItem> getShoppingItems() {
        return items;
    }
}