class AddColumnDrawxDrawy < ActiveRecord::Migration
  def up
  	add_column :draws, :drawx, :text
  	add_column :draws, :drawy, :text
  end

  def down
  end
end
