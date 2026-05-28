package com.tommy.bookstore.tasks;

import com.tommy.bookstore.entity.BookstoreService;
import com.tommy.bookstore.entity.Magazine;
import com.tommy.bookstore.entity.Publisher;

import java.util.List;

public class BookstoreApplication {

    public static void main(String[] args) {
//		BookstoreService bookstoreService = new BookstoreService();
//		Publisher publisher = bookstoreService.findPublisherById(2L);
//		System.out.println(publisher.toString());
//		List<Magazine> magazineList = bookstoreService.getAllMagazines(2L);
//
//		for (Magazine m : magazineList) {
//			if (m != null) {
//				System.out.println(m);
//			}
//		}
//
//		System.out.println(magazineList);


//	BookstoreService bookstoreService = new BookstoreService();
//	Publisher publisher = bookstoreService.findPublisherById(2L);
//	task 4 --> transaction ended
//		System.out.println(publisher.getMagazines());
//	for (Magazine m : publisher.getMagazines()) {
//		System.out.println(m);
//	}


        BookstoreService bookstoreService = new BookstoreService();
        List<Publisher> list = bookstoreService.findAllpublisher();

        for (Publisher p : list) {
            System.out.println(p.getMagazines().toString());
        }
    }
}