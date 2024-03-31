package com.example.cryptomarketlive.transformer;

import com.example.cryptomarketlive.entity.Order;
import com.example.cryptomarketlive.entity.OrderBook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

class MessageTransformerTest {

    private static final String PAIR = "XBT/USD";

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private OrderBook orderBook;
    @InjectMocks
    private MessageTransformer messageTransformer;

    @Captor
    private ArgumentCaptor<NavigableSet<Order>> askOrdersCaptor;
    @Captor
    private ArgumentCaptor<NavigableSet<Order>> bidOrdersCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        askOrdersCaptor = ArgumentCaptor.forClass(NavigableSet.class);
        bidOrdersCaptor = ArgumentCaptor.forClass(NavigableSet.class);
    }

    @Test
    void transformMessageTest() throws Exception {
        //Given
        String testMessage = """
                [
                  0,
                  {
                    "as": [
                      [
                        "5541.30000",
                        "2.50700000",
                        "1534614248.123678"
                      ],
                      [
                        "5541.80000",
                        "0.33000000",
                        "1534614098.345543"
                      ],
                      [
                        "5542.70000",
                        "0.64700000",
                        "1534614244.654432"
                      ]
                    ],
                    "bs": [
                      [
                        "5541.20000",
                        "1.52900000",
                        "1534614248.765567"
                      ],
                      [
                        "5539.90000",
                        "0.30000000",
                        "1534614241.769870"
                      ],
                      [
                        "5539.50000",
                        "5.00000000",
                        "1534613831.243486"
                      ]
                    ]
                  },
                  "book-100",
                  "XBT/USD"
                ]""";

        NavigableSet<Order> orders = new TreeSet<>();
        ConcurrentHashMap<String, NavigableSet<Order>> asks = new ConcurrentHashMap<>();
        asks.put(PAIR, orders);
        ConcurrentHashMap<String, NavigableSet<Order>> bids = new ConcurrentHashMap<>();
        bids.put(PAIR, orders);
        JsonNode payload = new ObjectMapper().readTree(testMessage);

        //When
        when(objectMapper.readTree(testMessage)).thenReturn(payload);
        when(orderBook.getAsks()).thenReturn(asks);
        when(orderBook.getBids()).thenReturn(bids);

        //Then
        messageTransformer.transformMessage(testMessage);

        verify(orderBook, times(1)).initializeDataForPair(PAIR);
        verify(orderBook, times(1)).updateOrderBook(eq(PAIR), any(), any());
        verify(orderBook).updateOrderBook(eq(PAIR), askOrdersCaptor.capture(), bidOrdersCaptor.capture());

        NavigableSet<Order> capturedAsks = askOrdersCaptor.getValue();
        NavigableSet<Order> capturedBids = bidOrdersCaptor.getValue();
        Assertions.assertEquals(6, capturedAsks.size());
        Assertions.assertEquals(6, capturedBids.size());
    }
}