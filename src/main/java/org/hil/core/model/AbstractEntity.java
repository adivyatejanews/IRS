/*
  GRANITE DATA SERVICES
  Copyright (C) 2007-2008 ADEQUATE SYSTEMS SARL

  This file is part of Granite Data Services.

  Granite Data Services is free software; you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation; either version 3 of the License, or (at your
  option) any later version.

  Granite Data Services is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
  for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, see <http://www.gnu.org/licenses/>.
 */

package org.hil.core.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Franck WOLFF
 */

@MappedSuperclass
@EntityListeners(AbstractEntity.AbstractEntityListener.class)
@org.hibernate.annotations.GenericGenerator(name = "hibernate-uuid", strategy = "uuid.hex")
public abstract class AbstractEntity
{

	/**
	 * 
	 */
	public AbstractEntity()
	{
		uid = java.util.UUID.randomUUID().toString();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	/* "UUID" and "UID" are Oracle reserved keywords -> "ENTITY_UID" */

	@Column(name = "entity_uid", unique = true, nullable = false, updatable = false, length = 36)
	private String uid = null;

	public String getUid()
	{
		if (uid == null)
		{
			uid = java.util.UUID.randomUUID().toString();
		}
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public Long getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	@Override
	public boolean equals(Object o)
	{
		return (o == this || (o instanceof AbstractEntity && uid().equals(
				((AbstractEntity) o).uid())));
	}

	@Override
	public int hashCode()
	{
		return uid().hashCode();
	}

	public static class AbstractEntityListener
	{

		@PrePersist
		public void onPrePersist(AbstractEntity abstractEntity)
		{
			abstractEntity.uid();
		}
	}

	private String uid()
	{
		if (uid == null)
			uid = UUID.randomUUID().toString();
		return uid;
	}

	/**
	 * method for jaxb
	 */
	public String marshallObject(Object item) throws JAXBException
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(item.getClass());
			Marshaller marshaller = context.createMarshaller();
			StringWriter sw = new StringWriter();
			marshaller.marshal(item, sw);
			return sw.toString();
		} catch (NullPointerException ex)
		{
			// ex.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public Object unmarshallObject(String xml, Class clazz)
			throws JAXBException
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(null);
			Object xmlObject = clazz.cast(unmarshaller
					.unmarshal(new StreamSource(new StringReader(xml))));
			return xmlObject;
		} catch (NullPointerException ex)
		{
			// ex.printStackTrace();
			return null;
		}

	}	

}