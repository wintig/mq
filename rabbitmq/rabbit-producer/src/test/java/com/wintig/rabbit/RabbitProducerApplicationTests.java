package com.wintig.rabbit;

import com.wintig.rabbit.entity.Order;
import com.wintig.rabbit.producer.OrderSender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitProducerApplicationTests {

	@Autowired
	private OrderSender orderSender;

	@Test
	public void testSend1() throws Exception {

		Order order = new Order();
		order.setId("201903310001");
		order.setName("测试");
		order.setMessageID(System.currentTimeMillis() + "$" + UUID.randomUUID());

		orderSender.send(order);
	}

	@Test
	public void testSend2() throws Exception {

		List<Book> list = new ArrayList<>();

		Book book = new Book("张三", "123");
		Book book2 = new Book("张三", "456");
		Book book3 = new Book("李四", "123");
		Book book4 = new Book("李四", "456");
		Book book5 = new Book("王五", "123");
		Book book6 = new Book("王五", "456");

		list.add(book);
		list.add(book6);
		list.add(book3);
		list.add(book2);
		list.add(book5);
		list.add(book4);


		// 过滤之前
		System.out.println("过滤钱===============");
		System.out.println(list.toString());


		System.out.println("过滤后===============");
		Collections.sort(list, new Comparator<Book>() {
			@Override
			public int compare(Book o1, Book o2) {
				return o1.getBookName().compareTo(o2.getBookName());
			}
		});


		System.out.println(list.toString());
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class Book  {

		private String bookName;

		private String url;


	}

}
